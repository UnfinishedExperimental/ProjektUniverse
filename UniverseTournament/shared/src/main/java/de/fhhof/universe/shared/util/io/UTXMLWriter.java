package de.fhhof.universe.shared.util.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasse, deren Instanzen mit hilfe von XStream Objekte im XML-Format auf
 * die Festplatte schreiben können.
 * 
 * @author Daniela Geilert
 *
 */
public class UTXMLWriter
{
    private static final Logger logger = Logger.getLogger(
    		UTXMLWriter.class.getName());
	private final XStream xstream;

	/**
	 * Erzeugt einen neuen XMLWriter mit eigener XStream-Instanz.
	 */
	public UTXMLWriter()
	{
		xstream = new XStream();
	}

	/**
	 * Schreibt das übergebene Objekt im XML Format auf die Festplatte an
	 * die vom File-Objekt angegebene Position, falls möglich.
	 * Ignoriert Aufrufe, bei denen Parameter null sind.
	 *
	 * @param f in welche Datei geschrieben werden soll
	 * @param o zu schreibendes Objekt
	 * @return Erfolg
	 */
	public boolean write(File f, Object o)
	{
		boolean success = true;

		if (o != null && f != null)
		{
			String xml = xstream.toXML(o);
			BufferedWriter bWriter = null;

			try
			{
				bWriter = new BufferedWriter(new FileWriter(f));
				bWriter.write(xml);
			}
			catch (Exception e)
			{
				e.printStackTrace();
                                logger.log(Level.WARNING, f.getAbsolutePath()
						+ " konnte nicht geschrieben werden.");
				success = false;
			}
			finally
			{
				try
				{
					bWriter.flush();
					bWriter.close();
				}
				catch (IOException e)
				{
					/*
					 * hier sind Fehler zu erwarten, fehlgeschlagener Vorgang
					 * wurde aber schon gemeldet
					 */
				}
			}

		}

		return success;
	}
}
