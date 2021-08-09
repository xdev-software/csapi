
package xdev.ui;

/*-
 * #%L
 * XDEV Component Suite
 * %%
 * Copyright (C) 2011 - 2021 XDEV Software
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */


import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import xdev.db.DBException;
import xdev.db.DataType;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;


public class TestVtCreator
{
	public final static String	COLUMN_NAME_PATTERN				= "Column";
	public final static String	TREETABLE_ID					= "Id ";
	public final static String	TREETABLE_PARENT_ID				= "ParentId ";
	public final static String	TREETABLE_PARENT_IDENTIFIER		= "Parent";
	
	// the count of the nodes per level - e.g. lowest tree level is always
	// item*nodeperlevelcount.
	public final static Integer	TREETABLE_NODE_PER_LEVEL_COUNT	= 3;
	
	// the columnIndex for test validations
	public final static Integer	TREETABLE_PARENTID_COLUMN	= 1;
	
	public final static Integer	TREETABLE_ID_COLUMN	= 0;
	
	
	public static VirtualTable createVt(String name, String alias, int columnCount, int rowCount)
	{
		Random rand = new Random();
		@SuppressWarnings("rawtypes")
		VirtualTableColumn[] columns = new VirtualTableColumn[columnCount];
		for(int i = 0; i < columnCount; i++)
		{
			columns[i] = new VirtualTableColumn<String>(COLUMN_NAME_PATTERN + i);
			;
		}
		VirtualTable vt = new VirtualTable(name,alias,columns);
		try
		{
			for(int r = 0; r < rowCount; r++)
			{
				List<String> values = new ArrayList<String>();
				for(int c = 0; c < columnCount; c++)
				{
					values.add("" + rand.nextInt(1000));
				}
				vt.addRow(values,false);
			}
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		return vt;
	}
	
	
	public static VirtualTable createVt(String name, String alias)
	{
		return createVt(name,alias,10,100);
	}
	
	
	public static VirtualTable createTreeVt_sorted(String name, String alias)
	{
		@SuppressWarnings("rawtypes")
		VirtualTableColumn[] columns = new VirtualTableColumn[3];
		
		columns[0] = new VirtualTableColumn<String>(TREETABLE_ID);
		columns[1] = new VirtualTableColumn<String>(TREETABLE_PARENT_ID);
		columns[2] = new VirtualTableColumn<String>("Name ");
		
		VirtualTable vt = new VirtualTable(name,alias,columns);
		try
		{
			for(int roots = 0; roots < TREETABLE_NODE_PER_LEVEL_COUNT; roots++)
			{
				
				List<String> values = new ArrayList<String>(3);
				values.add("" + roots);
				values.add(TREETABLE_PARENT_IDENTIFIER);
				values.add("root with id:" + roots);
				vt.addRow(values,false);
				
				for(int children = 0; children < TREETABLE_NODE_PER_LEVEL_COUNT; children++)
				{
					values = new ArrayList<String>(3);
					values.add("" + roots + "" + children);
					values.add("" + roots);
					values.add("child with id:" + children);
					vt.addRow(values,false);
					
					for(int grandchildren = 0; grandchildren < TREETABLE_NODE_PER_LEVEL_COUNT; grandchildren++)
					{
						values = new ArrayList<String>(3);
						values.add("" + roots + children + "" + grandchildren);
						values.add("" + roots + "" + children);
						values.add("grandchild with id:" + grandchildren);
						vt.addRow(values,false);
					}
					
				}
				
			}
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		return vt;
	}
	
	private static String[]		countries	= new String[]{"Deutschland","�sterreich","Schweiz",
			"Frankreich","Italien"			};
	
	private static Integer[]	numbers		= new Integer[]{2,1,3,5,4};
	
	private static Date[]		dates		= new Date[]{new GregorianCalendar(2010,0,1).getTime(),
			new GregorianCalendar(2009,0,1).getTime(),new GregorianCalendar(2008,0,1).getTime(),
			new GregorianCalendar(2007,0,1).getTime(),new GregorianCalendar(2006,0,1).getTime(),
			new GregorianCalendar(2011,0,1).getTime(),};
	
	
	@SuppressWarnings({"rawtypes","unchecked"})
	public static VirtualTable createGroupVt(String name, String alias)
	{
		Random rand = new Random();
		VirtualTableColumn[] columns = new VirtualTableColumn[4];
		columns[0] = new VirtualTableColumn<Integer>("Numbers");
		columns[0].setType(DataType.INTEGER);
		
		columns[1] = new VirtualTableColumn<String>("Countries");
		columns[1].setType(DataType.VARCHAR);
		
		columns[2] = new VirtualTableColumn<Boolean>("booleans");
		columns[2].setType(DataType.BOOLEAN);
		
		columns[3] = new VirtualTableColumn<Date>("dates");
		columns[3].setType(DataType.DATE);
		
		VirtualTable vt = new VirtualTable(name,alias,columns);
		try
		{
			for(int r = 0; r < 100; r++)
			{
				List values = new ArrayList();
				
				values.add(numbers[rand.nextInt(numbers.length)]);
				values.add(countries[rand.nextInt(countries.length)]);
				values.add(Boolean.valueOf(r % 2 == 0));
				values.add(dates[rand.nextInt(dates.length)]);
				
				vt.addRow(values,false);
			}
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		return vt;
	}
	
	
	@SuppressWarnings({"rawtypes","unchecked"})
	public static VirtualTable createBarChartVt(String name, String alias)
	{
		Random rand = new Random();
		
		VirtualTableColumn[] columns = new VirtualTableColumn[4];
		columns[0] = new VirtualTableColumn<Integer>("Countries");
		columns[0].setType(DataType.VARCHAR);
		
		columns[1] = new VirtualTableColumn<String>("Ints");
		columns[1].setType(DataType.INTEGER);
		
		columns[2] = new VirtualTableColumn<Boolean>("Doubles");
		columns[2].setType(DataType.DOUBLE);
		
		columns[3] = new VirtualTableColumn<Boolean>("Dates");
		columns[3].setType(DataType.DATE);
		
		VirtualTable vt = new VirtualTable(name,alias,columns);
		try
		{
			Date now = new Date();
			for(int r = 0; r < countries.length; r++)
			{
				List values = new ArrayList();
				values.add(countries[r]);
				values.add(rand.nextInt(100));
				values.add(rand.nextDouble() * 100);
				values.add(new Date(now.getTime() + (r * 24L * 60 * 60 * 1000)));
				
				vt.addRow(values,false);
			}
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		return vt;
	}
	
	
	@SuppressWarnings({"rawtypes","unchecked"})
	public static VirtualTable createSimpleChartVt(String name, String alias)
	{
		// Random rand = new Random();
		VirtualTableColumn[] columns = new VirtualTableColumn[3];
		columns[0] = new VirtualTableColumn<Integer>("countries");
		columns[0].setType(DataType.VARCHAR);
		columns[1] = new VirtualTableColumn<String>("dates");
		columns[1].setType(DataType.DATE);
		columns[2] = new VirtualTableColumn<String>("numbers");
		columns[2].setType(DataType.INTEGER);
		
		VirtualTable vt = new VirtualTable(name,alias,columns);
		try
		{
			for(int r = 0; r < countries.length; r++)
			{
				List values = new ArrayList();
				
				values.add(countries[r]);
				values.add(dates[r]);
				values.add(numbers[r]);
				vt.addRow(values,false);
			}
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
		return vt;
	}
	
}
