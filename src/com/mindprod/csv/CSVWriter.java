package com.mindprod.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Write CSV (Comma Separated Value)
 * files.
 *
 * This format is used my Microsoft Word and Excel.
 * Fields are separated by commas, and enclosed in
 * quotes if they contain commas or quotes.
 * Embedded quotes are doubled.
 * Embedded spaces do not normally require surrounding quotes.
 * The last field on the line is not followed by a comma.
 * Null fields are represented by two commas in a row.
 *
 * @author copyright (c) 2002-2004 Roedy Green  Canadian Mind Products
 * @version 1.0  2002 March 27
 *          1.1  2002 March 28
 *               - allow variable separator
 *               - add close method
 *          1.2  2002 April 23
 *               - put in to separate package
 *          1.3  2002 April 24
 *               - three levels of quoting
 *          1.4  2002 April 24
 *               - convenience constructor
 *               - put(null) now means nl.
 *          1.6 2002 May 25
 *               - allow choice of quote char 
 *          1.9 2002 November 14
 *               - trim parameter to control whether 
 *                 fields are trimmed of
 *                 lead/trail whitespace (blanks, Cr, Lf, Tab etc.)
 *                 before writing.
 */
public class CSVWriter
   {
   static final boolean DEBUGGING  = false;

   /**
    * Constructor
    * 
    * @param pw         Writer where fields will be written.
    * @param quoteLevel
    *                   0 = minimal quotes
    *                   1 = quotes also around fields containing spaces
    *                   2 = quotels around all fields.
    *                   whether or not they contain commas, quotes or spaces.
    * @param separator
    *                   field separator character, usually ',' in North America,
    *                   ';' in Europe and sometimes '\t' for tab.
    * @param quote
    *                   char to use to enclose fields containing a separator,
    *                   usually '\"'
    *
    * @param trim       true if writer should trim leading/trailing whitespace
    *                   (e.g. blank, cr, Lf, tab) before writing
    *                   the field.
    */
   public CSVWriter ( Writer pw, int quoteLevel, char separator, char quote, boolean trim)
      {

      if ( pw instanceof PrintWriter )
         {
         this.pw = (PrintWriter) pw;
         }
      else
         {
         this.pw = new PrintWriter(pw);
         }
      if ( this.pw == null )
         {
         throw new IllegalArgumentException("invalid Writer");
         }
      this.quoteLevel = quoteLevel;
      this.separator = separator;
      this.quote = quote;
      this.trim = trim;
      }

   /**
    * convenience Constructor, defaults to quotelevel 1, comma separator , trim
    *
    * @param pw     Writer where fields will be written.
    *
    */
   public CSVWriter ( Writer pw)
      {

      this(pw, 1, ',', '\"', true);
      }


   /**
    * PrintWriter where CSV fields will be written.
    */
   PrintWriter pw;

   /**
    * how much extra quoting you want
    */
   int quoteLevel;

   /**
    * field separator character, usually ',' in North America,
    * ';' in Europe and sometimes '\t' for tab.
    */
   char separator;

   /**
    * quote character, usually '\"'
    * '\'' for SOL used to enclose fields containing a separator character.
    */
   private char quote;

   /**
    * true if write should trim lead/trail whitespace
    * from fields before writing them.
    */
   private final boolean trim;

   /**
    * true if there has was a field previously written to
    * this line, meaning there is a comma pending to
    * be written.
    */
   boolean wasPreviousField = false;

   /**
     * Write one csv field to the file, followed by a separator
     * unless it is the last field on the line. Lead and trailing blanks will be removed.
     *
     * @param s      The string to write.  Any additional quotes or
     *               embedded quotes will be provided by put.
     *               Null means start a new line.
     */
   public void put(String s)
      {
      if ( pw == null )
         {
         throw new IllegalArgumentException("attempt to use a closed CSVWriter");
         }
      if ( s == null )
         {
         nl();
         return;
         }

      if ( wasPreviousField )
         {
         pw.print(separator);
         }
      if ( trim )
         {
         s = s.trim();
         }
      if ( s.indexOf(quote) >= 0 )
         {
         /* worst case, needs surrounding quotes and internal quotes doubled */
         pw.print (quote);
         for ( int i=0; i<s.length(); i++ )
            {
            char c = s.charAt(i);
            if ( c == quote )
               {
               pw.print(quote);
               pw.print(quote);
               }
            else
               {
               pw.print(c);
               }
            }
         pw.print (quote);
         }
      else if ( quoteLevel == 2 || quoteLevel == 1 && s.indexOf(' ') >= 0 || s.indexOf(separator) >= 0 )
         {
         /* need surrounding quotes */
         pw.print (quote);
         pw.print(s);
         pw.print (quote);
         }
      else
         {
         /* ordinary case, no surrounding quotes needed */
         pw.print(s);
         }
      /* make a note to print trailing comma later */
      wasPreviousField = true;
      }

   /**
    * Write a new line in the CVS output file to demark the end of record.
    */
   public void nl()
      {
      if ( pw == null )
         {
         throw new IllegalArgumentException("attempt to use a closed CSVSWriter");
         }
      /* don't bother to write last pending comma on the line */
      pw.print("\r\n"); /* windows conventions since this is a windows format file */
      wasPreviousField = false;
      }

   /**
    * Close the PrintWriter.
    */
   public void close()
      {
      if ( pw != null )
         {
         pw.close();
         pw = null;
         }
      }

   /**
    * Test driver
    *
    * @param args   not used
    */
   public static void main(String[] args)
      {
      if ( DEBUGGING )
         {
         try
            {
            // write out a test file
            CSVWriter csv = new CSVWriter(new FileWriter("temp.txt"), 1, ';', '\'', true);
            csv.put("abc");
            csv.put("def");
            csv.put("g h i");
            csv.put("jk,l");
            csv.put("m\"n\'o ");
            csv.nl();
            csv.put("m\"n\'o ");
            csv.put("    ");
            csv.put("a");
            csv.put("x,y,z");
            csv.put("x;y;z");
            csv.nl();
            csv.close();
            }
         catch ( IOException  e )
            {
            e.printStackTrace();
            System.out.println(e.getMessage());
            }
         } // end if
      } // end main
   } // end CSVWriter class.

