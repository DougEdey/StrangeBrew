/*
 * $Id: XmlTransformer.java,v 1.1 2004/11/20 08:31:29 tangent_ Exp $
 * Created on Nov 19, 2004
 * @author Christopher Cook
 *
 * This class performs an xsl transformation on an xml file and writes the
 * result to the specified output destination.
 */

package strangebrew;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

public class XmlTransformer {

	/**
	 * Performs an XSLT transformation and writes the results to a file.
	 * @param xmlFile
	 * @param xsltFile
	 * @param outputFile
	 * @throws Exception
	 */
	public static void toFile(File xmlFile, File xsltFile, File outputFile)
			throws Exception {

		TransformerFactory factory = TransformerFactory.newInstance();

		try {
			Transformer transformer = factory.newTransformer(
				new StreamSource(xsltFile));

			transformer.transform(new StreamSource(xmlFile),
				new StreamResult(new FileOutputStream(outputFile)));

		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * OVERLOAD: Performs an XSLT transformation and writes the results
	 * to a file.
	 * @param xmlFilespec
	 * @param xsltFilespec
	 * @param outputFilespec
	 * @throws Exception
	 */
	public static void toFile(String xmlFilespec, String xsltFilespec,
			String outputFilespec) throws Exception {

		toFile(new File(xmlFilespec), new File(xsltFilespec),
				new File(outputFilespec));
	}

	/**
	 * Performs an XSLT transformation and writes the results to
	 * the specified PrintStream.
	 * @param xmlFile
	 * @param xsltFile
	 * @param p
	 * @throws Exception
	 */
	public static void toPrintStream(File xmlFile, File xsltFile, PrintStream p)
			throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();

		try {
			Transformer transformer = factory.newTransformer(
					new StreamSource(xsltFile));

			transformer.transform(new StreamSource(xmlFile),
	    			new StreamResult(p));

	    } catch(Exception e) {
	    	System.out.println(e.getMessage());
	    }
	}

	/**
	 * OVERLOAD: Performs an XSLT transformation and writes the results to
	 * the specified PrintStream.
	 * @param xmlFilespec
	 * @param xsltFilespec
	 * @param p
	 * @throws Exception
	 */
	public static void toPrintStream(String xmlFilespec, String xsltFilespec,
			PrintStream p) throws Exception {
		
		toPrintStream(new File(xmlFilespec), new File(xsltFilespec), p);
	}

}