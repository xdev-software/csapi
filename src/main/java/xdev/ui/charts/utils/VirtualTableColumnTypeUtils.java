package xdev.ui.charts.utils;

/*-
 * #%L
 * XDEV BI Suite
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


import java.util.Date;

import xdev.db.DataType;
import xdev.vt.DataFlavor;
import xdev.vt.VirtualTable;
import xdev.vt.VirtualTableColumn;


/**
 * Utility class to differentiate {@link VirtualTableColumn} types with their
 * {@link DataType}.
 * 
 * @author XDEV Software jwill
 * 
 * @since 4.0
 */
public class VirtualTableColumnTypeUtils
{
	
	/**
	 * Mostly {@link VirtualTable#getValueAt(int, int)} returns an
	 * {@link Object} type value.
	 * <p>
	 * This method is used to identify a {@link Number} value from a
	 * <code>Object</code> type value.
	 * </p>
	 * 
	 * @param column
	 *            the {@link VirtualTableColumn} to check.
	 * 
	 * @return the confirmation value.
	 * 
	 * @see DataType
	 */
	@SuppressWarnings("unchecked")
	public static VirtualTableColumn<? extends Number> isNumericColumn(VirtualTableColumn<?> column)
	{
		if(column.getType().isNumeric())
		{
			return (VirtualTableColumn<? extends Number>)column;
		}
		else
		{
			throw new IllegalArgumentException(
					"Numeric column data type required for valueColumnIndex");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static VirtualTableColumn<? extends Number>[] isNumericColumns(
			VirtualTableColumn<?>[] columns)
	{
		VirtualTableColumn<? extends Number>[] retColumns = new VirtualTableColumn[columns.length];
		
		for(int i = 0; i < columns.length; i++)
		{
			if(columns[i].getType().isNumeric())
			{
				retColumns[i] = (VirtualTableColumn<? extends Number>)columns[i];
			}
			else
			{
				throw new IllegalArgumentException(
						"Numeric column data type required for valueColumnIndex");
			}
		}
		
		return retColumns;
	}
	
	
	@SuppressWarnings("unchecked")
	public static VirtualTableColumn<? extends Date>[] isDateColumns(VirtualTableColumn<?>[] columns)
	{
		VirtualTableColumn<? extends Date>[] retColumns = new VirtualTableColumn[columns.length];
		
		for(int i = 0; i < columns.length; i++)
		{
			if(columns[i].getType().isDate())
			{
				retColumns[i] = (VirtualTableColumn<? extends Date>)columns[i];
			}
			else
			{
				throw new IllegalArgumentException(
						"Date column data type required for valueColumnIndex");
			}
		}
		
		return retColumns;
	}
	
	
	@SuppressWarnings("unchecked")
	public static VirtualTableColumn<? extends String>[] isStringColumns(
			VirtualTableColumn<?>[] columns)
	{
		VirtualTableColumn<? extends String>[] retColumns = new VirtualTableColumn[columns.length];
		
		for(int i = 0; i < columns.length; i++)
		{
			if(columns[i].getType().isString())
			{
				retColumns[i] = (VirtualTableColumn<? extends String>)columns[i];
			}
			else
			{
				throw new IllegalArgumentException(
						"String column data type required for valueColumnIndex");
			}
		}
		
		return retColumns;
	}
	
	
	/**
	 * Mostly {@link VirtualTable#getValueAt(int, int)} returns an
	 * {@link Object} type value.
	 * <p>
	 * This method is used to identify a {@link Date} value from a
	 * <code>Object</code> type value.
	 * </p>
	 * 
	 * @param column
	 *            the {@link VirtualTableColumn} to check.
	 * 
	 * @return the confirmation value.
	 * 
	 * @see DataType
	 */
	@SuppressWarnings("unchecked")
	public static VirtualTableColumn<? extends Date> isDateColumn(VirtualTableColumn<?> column)
	{
		if(column.getType().isDate())
		{
			return (VirtualTableColumn<? extends Date>)column;
		}
		else
		{
			throw new IllegalArgumentException(
					"Date column data type required for valueColumnIndex");
		}
	}
	
	
	/**
	 * Mostly {@link VirtualTable#getValueAt(int, int)} returns an
	 * {@link Object} type value.
	 * <p>
	 * This method is used to identify a {@link String} value from a
	 * <code>Object</code> type value.
	 * </p>
	 * 
	 * @param column
	 *            the {@link VirtualTableColumn} to check.
	 * 
	 * @return the confirmation value.
	 * 
	 * @see DataType
	 */
	@SuppressWarnings("unchecked")
	public static VirtualTableColumn<String> isStringColumn(VirtualTableColumn<?> column)
	{
		if(column.getType().isString())
		{
			return (VirtualTableColumn<String>)column;
		}
		else
		{
			throw new IllegalArgumentException(
					"String column data type required for valueColumnIndex");
		}
	}
	
	
	/**
	 * Mostly {@link VirtualTable#getValueAt(int, int)} returns an
	 * {@link Object} type value.
	 * <p>
	 * This method is used to identify a {@link Integer} value from a
	 * <code>Object</code> type value.
	 * </p>
	 * 
	 * @param column
	 *            the {@link VirtualTableColumn} to check.
	 * 
	 * @return the confirmation value.
	 * 
	 * @see DataType
	 */
	public static boolean isIntColumn(VirtualTableColumn<?> column)
	{
		if(column.getType().isInt())
		{
			return true;
		}
		else
		{
			throw new IllegalArgumentException(
					"Integer column data type required for valueColumnIndex");
		}
	}
	
	
	/**
	 * Mostly {@link VirtualTable#getValueAt(int, int)} returns an
	 * {@link Object} type value.
	 * <p>
	 * This method is used to identify a <code>Decimal</code> value from a
	 * <code>Object</code> type value.
	 * </p>
	 * 
	 * @param column
	 *            the {@link VirtualTableColumn} to check.
	 * 
	 * @return the confirmation value.
	 * 
	 * @see DataType
	 */
	public static boolean isDecimalColumn(VirtualTableColumn<?> column)
	{
		if(column.getType().isDecimal())
		{
			return true;
		}
		else
		{
			throw new IllegalArgumentException(
					"Decimal column data type required for valueColumnIndex");
		}
	}
	
	
	public static VirtualTableColumn<?> getColorColumn(VirtualTable columnOwner)
	{
		for(int i = 0; i < columnOwner.getColumnCount(); i++)
		{
			VirtualTableColumn<?> colorColumn = columnOwner.getColumnAt(i);
			
			if(colorColumn.getDataFlavor().equals(DataFlavor.COLOR))
			{
				return colorColumn;
			}
		}
		return null;
	}
}
