/*
 * DirectoryListSet.java - Directory list matcher
 * :tabSize=4:indentSize=4:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1999, 2000, 2001 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.search;

//{{{ Imports
import java.awt.Component;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.gjt.sp.jedit.io.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.StandardUtilities;
//}}}

/**
 * Recursive directory search.
 * @author Slava Pestov
 * @version $Id: DirectoryListSet.java 22454 2012-11-10 11:15:08Z thomasmey $
 */
public class DirectoryListSet extends BufferListSet
{
	//{{{ DirectoryListSet constructor
	public DirectoryListSet(String directory, String glob, boolean recurse)
	{
		this.directory = directory;
		this.glob = glob;
		this.recurse = recurse;
		this.modifiedFrom = "2020/02/05";
		this.modifiedTo = "2020/05/05";
		this.filterModifiedDate = false;
	} //}}}

	//{{{ DirectoryListSet constructor with filterModifiedDate
	public DirectoryListSet(String directory, String glob, boolean recurse,
							String modifiedFrom, String modifiedTo)
	{
		this.directory = directory;
		this.glob = glob;
		this.recurse = recurse;
		this.modifiedFrom = modifiedFrom;
		this.modifiedTo = modifiedTo;
		this.filterModifiedDate = true;
	} //}}}

	//{{{ getDirectory() method
	public String getDirectory()
	{
		return directory;
	} //}}}

	//{{{ setDirectory() method
	/**
	 * @since jEdit 4.2pre1
	 */
	public void setDirectory(String directory)
	{
		this.directory = directory;
		invalidateCachedList();
	} //}}}

	//{{{ getFileFilter() method
	public String getFileFilter()
	{
		return glob;
	} //}}}

	//{{{ getModifiedFrom() method
	public String getModifiedFrom()
	{
		return modifiedFrom;
	} //}}}

	//{{{ getModifiedTo() method
	public String getModifiedTo()
	{
		return modifiedTo;
	} //}}}

	//{{{ setFileFilter() method
	/**
	 * @since jEdit 4.2pre1
	 */
	public void setFileFilter(String glob)
	{
		this.glob = glob;
		invalidateCachedList();
	} //}}}

	//{{{ setModifiedFrom() method
	/**
	 * @since jEdit 4.2pre1
	 */
	public void setFilterModifiedDate(boolean filterModifiedDate,
									  String modifiedFrom, String modifiedTo)
	{
		this.filterModifiedDate = filterModifiedDate;
		this.modifiedFrom = modifiedFrom;
		this.modifiedTo = modifiedTo;
		invalidateCachedList();
	} //}}}


	//{{{ isRecursive() method
	public boolean isRecursive()
	{
		return recurse;
	} //}}}

	//{{{ setRecursive() method
	/**
	 * @since jEdit 4.2pre1
	 */
	public void setRecursive(boolean recurse)
	{
		this.recurse = recurse;
		invalidateCachedList();
	} //}}}

	//{{{ getCode() method
	@Override
	public String getCode()
	{
		return "new DirectoryListSet(\"" + StandardUtilities.charsToEscapes(directory)
			+ "\",\"" + StandardUtilities.charsToEscapes(glob) + "\","
			+ recurse + ')';
	} //}}}

	//{{{ _getFiles() method
	@Override
	protected String[] _getFiles(final Component comp)
	{
		boolean skipBinary, skipHidden;
		skipBinary = jEdit.getBooleanProperty("search.skipBinary.toggle");
		skipHidden = jEdit.getBooleanProperty("search.skipHidden.toggle");
		final VFS vfs = VFSManager.getVFSForPath(directory);
		Object session;
		session = vfs.createVFSSessionSafe(directory, comp);

		try
		{
			try
			{
				String[] files = vfs._listDirectory(session,directory,glob,recurse,comp, skipBinary, skipHidden);

				//filter modified date
				if (this.filterModifiedDate){
					File file;
					ArrayList<String> fileListTemp = new ArrayList<String>();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

				    for (int i = 0; i < files.length; i++) {
                        file = new File(files[i]);
                        if (sdf.parse(this.modifiedFrom).getTime() < file.lastModified() &&
								file.lastModified() < sdf.parse(this.modifiedTo).getTime()){
							fileListTemp.add(files[i]);
						}
					}
					files = fileListTemp.toArray(new String[fileListTemp.size()]);
				}

				return files;
			}
			finally
			{
				vfs._endVFSSession(session, comp);
			}
		}
		catch(IOException | ParseException io)
		{
			VFSManager.error(comp,directory,"ioerror",new String[]
				{ io.toString() });
			return null;
		}
	} //}}}

	//{{{ Private members
	private String directory;
	private String glob;
	private boolean recurse;

	private String modifiedFrom;
	private String modifiedTo;
	private boolean filterModifiedDate;
	//}}}
}
