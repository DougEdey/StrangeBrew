/**
 * $Id: XmlTransformer.java,v 1.2 2004/11/20 18:32:05 tangent_ Exp $
 * Created on Nov 19, 2004
 * @author Christopher Cook
 *
 * This class performs an xsl transformation on an xml source and writes the
 * result to an Output Stream.
 * 
 * The writeStream method is static so can be called without instantiating
 * an instance of the class. Example:
 * <code>
 * 	String xmlFilespec  = "data/file.xml";
 * 	String xsltFilespec = "data/xmlToHtml.xslt";
 * 	String htmlFilespec = "data/file.html";
 * 	java.io.File htmlFile = new java.io.File(htmlFilespec);
 * 	java.io.FileOutputStream output = new java.io.FileOutputStream(htmlFile);
 *
 * 	XmlTransformer.writeStream(xmlFilespec, xsltFilespec, output);
 * </code>
 */

package strangebrew;

import java.io.File;
import java.io.OutputStream;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

public class XmlTransformer {

	/**
	 * Performs an XSLT transformation and writes the result to
	 * the specified OutputStream.
	 * 
	 * @param xml			the source xml
	 * @param xslt			the source xslt
	 * @param output		an output stream (like FileOutputStream)
	 * @throws Exception
	 */
	public static void writeStream(Source xml, Source xslt,
			OutputStream output) throws Exception {
		
		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			Transformer transformer = factory.newTransformer(xslt);
			transformer.transform(xml, new StreamResult(output));
	    } catch(Exception e) {
	    	System.out.println(e.getMessage());
	    }
	}

	/**
	 * Performs an XSLT transformation and writes the result to
	 * the specified OutputStream. This overload accepts Files strings instead
	 * of actual StreamSource.
	 * 
	 * @param xmlFile		the file containing the source xml
	 * @param xsltFile		the file containing the source xslt
	 * @param output		an output stream (like System.out)
	 * @throws Exception
	 */
	public static void writeStream (File xmlFile, File xsltFile,
			OutputStream output) throws Exception {

		writeStream(new StreamSource(xmlFile),
				new StreamSource(xsltFile), output);
	}

	/**
	 * Performs an XSLT transformation and writes the results to the
	 * specified OutputStream. This overload accepts filespec strings instead
	 * of actual StreamSource.
	 * 
	 * @param xmlFilespec		path to the xml source file
	 * @param xsltFilespec		path to the xslt source file
	 * @param output			an output stream (like System.out)
	 * @throws Exception
	 */
	public static void writeStream (String xmlFilespec, String xsltFilespec,
			OutputStream output) throws Exception {
		
		writeStream(new File(xmlFilespec), new File(xsltFilespec), output);
	}

}