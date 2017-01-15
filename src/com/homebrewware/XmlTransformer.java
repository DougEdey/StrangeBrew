/**
 *    Filename: XmlTransformer.java
 *     Version: 0.9.0
 * Description: XML Transformer
 *     License: GPLv2
 *        Date: 2017-01-14
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
 * Copyright (c) 2004-2006 Drew Avis
 * @author Drew Avis
 *
 * Copyright (c) 2004 Christopher Cook
 * @author Christopher Cook
 *
 * Copyright (c) 2017 Dallas Fletchall
 * @author Dallas Fletchall
 */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.homebrewware;

import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XmlTransformer {

	/**
	 * Performs an XSLT transformation and writes the result to
	 * the specified OutputStream.
	 *
	 * @param xml			the source xml
	 * @param xslt			the source xslt
	 * @param output		an output stream (like FileOutputStream)
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static void writeStream(Source xml, Source xslt,
			OutputStream output)
			throws TransformerConfigurationException, TransformerException {

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(xslt);
		transformer.transform(xml, new StreamResult(output));
	}

	/**
	 * Performs an XSLT transformation and writes the result to
	 * the specified OutputStream. This overload accepts Files strings instead
	 * of actual StreamSource.
	 *
	 * @param xmlFile		the file containing the source xml
	 * @param xsltFile		the file containing the source xslt
	 * @param output		an output stream (like System.out)
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static void writeStream (File xmlFile, File xsltFile,
			OutputStream output)
			throws TransformerConfigurationException, TransformerException {

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
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static void writeStream (String xmlFilespec, String xsltFilespec,
			OutputStream output)
			throws TransformerConfigurationException, TransformerException {

		writeStream(new File(xmlFilespec), new File(xsltFilespec), output);
	}

	/**
	 * Performs an XSLT transformation and writes the results to the
	 * specified OutputStream. This overload accepts a mixed set of types
	 * for the xml and xslt arguments instead of actual StreamSource.
	 *
	 * @param xmlFilespec		path to the xml source file
	 * @param xsltFilespec		path to the xslt source file
	 * @param output			an output stream (like System.out)
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	public static void writeStream(Source xml, File xsltFile,
			OutputStream output)
			throws TransformerConfigurationException, TransformerException {

		writeStream(xml, new StreamSource(xsltFile), output);
	}

	public static String getString(Source xml, File xsltFile)
			throws TransformerConfigurationException, TransformerException{
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));
		StringWriter writer = new StringWriter();
		transformer.transform(xml, new StreamResult(writer));
		return writer.getBuffer().toString();
	}

}
