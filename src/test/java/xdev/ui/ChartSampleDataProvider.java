
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
import java.util.List;
import java.util.Random;

import com.jidesoft.chart.Chart;

import xdev.db.DBException;
import xdev.db.DataType;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;
import xdev.vt.VirtualTableException;


/**
 * A {@link VirtualTable} based sample model provider for {@link Chart}
 * implementations.
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public final class ChartSampleDataProvider
{
	private static ChartSampleDataProvider	modelProvider	= null;
	
	
	/**
	 * Returns the singleton {@link ChartSampleDataProvider} instance.
	 * 
	 * @return the singleton model provider instance.
	 */
	public static ChartSampleDataProvider getInstance()
	{
		if(modelProvider != null)
		{
			return modelProvider;
		}
		else
		{
			modelProvider = new ChartSampleDataProvider();
			return modelProvider;
		}
	}
	
	
	private ChartSampleDataProvider()
	{
	}
	
	private static String[]		string_categories				= new String[]{"Apple","IBM",
			"Mircosoft","Oracle","Amazon","Evernote"			};										// "PayPal","Steam","Origin","Windows","MacOs","Linux"
	//
	// ,"XDEV","Google","Ebay","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
	// "p","q","r","s","t","u","v","w","x","y","z"
	// };
	
	private static Integer[]	number_categories				= new Integer[]{1,2,3,4};
	
	private static Integer[]	integer_values					= new Integer[]{6,6,9,18,13,15,4,
			20,1,14,16,11,12,0,7								};
	
	private static String[]		string_group_values_years		= new String[]{"2010","2011",
			"2012","2013"										};
	private static Integer[]	integer_group_values_years		= new Integer[]{2010,2011,2012,2013};
	
	public static final int		STRING_CATEGORY_COLUMN_INDEX	= 0;
	public static final int		INTEGER_CATEGORY_COLUMN_INDEX	= 0;
	public static final int		INTEGER_VALUES1_COLUMN_INDEX	= 1;
	public static final int		INTEGER_VALUES2_COLUMN_INDEX	= 2;
	public static final int		INTEGER_VALUES3_COLUMN_INDEX	= 3;
	public static final int		DEPTH_COLUMN_INDEX				= 4;
	public static final int		COLOR_COLUMN_INDEX				= 2;
	public static final int		STRING_GROUP_INDEX				= 2;
	public static final int		INTEGER_GROUP_INDEX				= 2;
	
	
	/**
	 * Returns a {@link VirtualTable} as multi series sample chart model.
	 * 
	 * @param name
	 *            the sample {@link VirtualTable} name.
	 * @param alias
	 *            the sample {@link VirtualTable} alias.
	 * @return the sample model as {@link VirtualTable}.
	 */
	public static VirtualTable createDynamicStructureMultiSeriesChartSampleDataVT(String name,
			String alias)
	{
		VirtualTableColumn<?>[] columns = new VirtualTableColumn<?>[3];
		columns[0] = new VirtualTableColumn<Integer>("Categories");
		columns[0].setType(DataType.INTEGER);
		
		columns[1] = new VirtualTableColumn<Integer>("Values");
		columns[1].setName("Values");
		columns[1].setType(DataType.INTEGER);
		
		columns[2] = new VirtualTableColumn<Integer>("YEAR_GROUP");
		columns[2].setName("Year");
		columns[2].setType(DataType.INTEGER);
		
		VirtualTable vt = new VirtualTable(name,alias,columns);
		try
		{
			List<Object> values = new ArrayList<Object>();
			values.add(number_categories[0]);
			values.add(integer_values[0]);
			values.add(integer_group_values_years[0]);
			vt.addRow(values,false);
			
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
	
	
	/**
	 * Returns a {@link VirtualTable} as multi series sample chart model.
	 * 
	 * @param name
	 *            the sample {@link VirtualTable} name.
	 * @param alias
	 *            the sample {@link VirtualTable} alias.
	 * @return the sample model as {@link VirtualTable}.
	 */
	public static void createDynamicStructureMultiSeriesChartSampleDataVT(VirtualTable vt)
	{
		VirtualTableColumn<?>[] columns = new VirtualTableColumn<?>[3];
		columns[0] = new VirtualTableColumn<Integer>("Categories");
		columns[0].setType(DataType.INTEGER);
		
		columns[1] = new VirtualTableColumn<Integer>("Values");
		columns[1].setName("Values");
		columns[1].setType(DataType.INTEGER);
		
		columns[2] = new VirtualTableColumn<Integer>("YEAR_GROUP");
		columns[2].setName("Year");
		columns[2].setType(DataType.INTEGER);
		
		try
		{
			List<Object> values = new ArrayList<Object>();
			values.add(number_categories[0]);
			values.add(integer_values[0]);
			values.add(integer_group_values_years[0]);
			vt.addRow(values,false);
		}
		catch(VirtualTableException e)
		{
			e.printStackTrace();
		}
		catch(DBException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Returns a {@link VirtualTable} as multi series sample chart model.
	 * 
	 * @param name
	 *            the sample {@link VirtualTable} name.
	 * @param alias
	 *            the sample {@link VirtualTable} alias.
	 * @return the sample model as {@link VirtualTable}.
	 */
	public static VirtualTable createDynamicStructureMultiSeriesChartSampleDataVT_StringCategories(
			String name, String alias)
	{
		VirtualTableColumn<?>[] columns = new VirtualTableColumn<?>[3];
		columns[0] = new VirtualTableColumn<String>("Categories");
		columns[0].setType(DataType.VARCHAR);
		
		columns[1] = new VirtualTableColumn<Integer>("Values");
		columns[1].setName("Values");
		columns[1].setType(DataType.INTEGER);
		
		columns[2] = new VirtualTableColumn<String>("YEAR_GROUP");
		columns[2].setName("Year");
		columns[2].setType(DataType.VARCHAR);
		
		VirtualTable vt = new VirtualTable(name,alias,columns);
		try
		{
			Random rand = new Random();
			for(int r = 0; r < string_categories.length; r++)
			{
				List<Object> values = new ArrayList<Object>();
				values.add(string_categories[r]);
				values.add(rand.nextInt(30));
				values.add(string_group_values_years[(r % 2) == 0 ? 0 : 1]);
				
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
