package nl.irp.sepa;

import com.google.common.io.Resources;
import iso.std.iso._20022.tech.xsd.pain_001_001.Document;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringWriter;

import static junit.framework.Assert.assertEquals;

public class ParsePainTest {

    @Test
    public void testParseTransparency() throws IOException, JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Document.class);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Document unmarshal = unmarshaller.unmarshal(new StreamSource(
                Resources.getResource("abn/pain.001.001.03 voorbeeldbestand.xml").openStream()
        ), Document.class).getValue();

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // The UTF-8 character encoding standard must be used in the UNIFI messages.
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        try (StringWriter stringWriter = new StringWriter()) {
            marshaller.marshal(unmarshal, stringWriter);
            Document actual = unmarshaller.unmarshal(new StreamSource(new StringBufferInputStream(stringWriter.toString())), Document.class).getValue();
            assertEquals(actual, unmarshal);
        }
    }

}
