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


import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import xdev.category.UITest;

/**
 * 
 * Tests regarding {@link XdevTabbedPane2}.
 * 
 * @author XDEV Software
 * 
 * @since CS 1.0
 */

/*
 * --------------------------------------------------------------------------------------------------------------------------------------
 *  PROPERTY - TESTS (-> to test the general functionality of the components GUI-appearance modifications as in the XDEV3-CS integration)
 *  for example if specific property components ar functional correct displayed.
 * --------------------------------------------------------------------------------------------------------------------------------------
 */
@Category(UITest.class)
public class XdevTabbedPane2JTest
{
	/**
	 * {@link XdevTabbedPane2#setColorTheme(int)}.
	 */
	@Test
	public void setColorTheme1()
	{
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setColorTheme(XdevTabbedPane2.COLOR_THEME_OFFICE2003);
		Assert.assertEquals(XdevTabbedPane2.COLOR_THEME_OFFICE2003, tp.getColorTheme());
	}
	
	/**
	 * {@link XdevTabbedPane2#setColorTheme(int)}.
	 */
	@Test
	public void setColorTheme2()
	{
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setColorTheme(XdevTabbedPane2.COLOR_THEME_VSNET);
		Assert.assertEquals(XdevTabbedPane2.COLOR_THEME_VSNET, tp.getColorTheme());
	}
	
	/**
	 * {@link XdevTabbedPane2#setTabShape(int)}.
	 */
	@Test
	public void setTabShape1()
	{
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setTabShape(XdevTabbedPane2.SHAPE_VSNET);
		Assert.assertEquals(XdevTabbedPane2.SHAPE_VSNET, tp.getTabShape());
	}
	
	/**
	 * {@link XdevTabbedPane2#setTabShape(int)}.
	 */
	@Test
	public void setTabShape2()
	{
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setTabShape(XdevTabbedPane2.SHAPE_EXCEL);
		Assert.assertEquals(XdevTabbedPane2.SHAPE_EXCEL, tp.getTabShape());
	}
	
	//to test the shape and color interaction
	/**
	 * {@link XdevTabbedPane2#setTabShape(int)}.
	 * {@link XdevTabbedPane2#setColorTheme(int)}.
	 */
	@Test
	public void colorShape_combination()
	{
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setTabShape(XdevTabbedPane2.SHAPE_EXCEL);
		tp.setColorTheme(XdevTabbedPane2.COLOR_THEME_VSNET);
		Assert.assertEquals(XdevTabbedPane2.SHAPE_EXCEL, tp.getTabShape());
		Assert.assertEquals(XdevTabbedPane2.COLOR_THEME_VSNET, tp.getColorTheme());
	}
	
	//to test the shape and color interaction
	/**
	 * {@link XdevTabbedPane2#setTabShape(int)}.
	 * {@link XdevTabbedPane2#setColorTheme(int)}.
	 */
	@Test
	public void colorShape_combination2()
	{
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setTabShape(XdevTabbedPane2.SHAPE_VSNET);
		tp.setColorTheme(XdevTabbedPane2.COLOR_THEME_OFFICE2003);
		Assert.assertEquals(XdevTabbedPane2.SHAPE_VSNET, tp.getTabShape());
		Assert.assertEquals(XdevTabbedPane2.COLOR_THEME_OFFICE2003, tp.getColorTheme());
	}
	
	/**
	 * {@link XdevTabbedPane2#setShowCloseButtonOnSelectedTab(boolean)}.
	 * {@link XdevTabbedPane2#setShowCloseButton(boolean)}.
	 */
	//to test the combined close interactions
	@Test
	public void closeCombination1()
	{
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setShowCloseButtonOnSelectedTab(true);
		tp.setShowCloseButton(true);
		Assert.assertTrue(tp.isShowCloseButtonOnSelectedTab());
		Assert.assertTrue(tp.isShowCloseButton());
	}
	
	/**
	 * {@link XdevTabbedPane2#setShowCloseButtonOnTab(boolean)}.
	 * {@link XdevTabbedPane2#setShowCloseButton(boolean)}.
	 */
	//to test the combined close interactions
	@Test
	public void closeCombination2()
	{
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setShowCloseButtonOnTab(true);
		tp.setShowCloseButton(true);
		Assert.assertTrue(tp.isShowCloseButton());
		Assert.assertTrue(tp.isShowCloseButtonOnTab());
	}
	
	/**
	 * {@link XdevTabbedPane2#setShowCloseButtonOnTab(boolean)}.
	 * {@link XdevTabbedPane2#setShowCloseButtonOnSelectedTab(boolean)}.
	 */
	//to test the combined close interactions
	@Test
	public void closeCombination3()
	{
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setShowCloseButtonOnTab(true);
		tp.setShowCloseButtonOnSelectedTab(true);
		Assert.assertTrue(tp.isShowCloseButtonOnSelectedTab());
		Assert.assertTrue(tp.isShowCloseButtonOnTab());
	}
	
	/**
	 * {@link XdevTabbedPane2##setBoldActiveTab(boolean)}.
	 * {@link XdevTabbedPane2##setShowGripper(boolean)}.
	 * {@link XdevTabbedPane2##setShowCloseButtonOnTab(boolean)}.
	 * {@link XdevTabbedPane2##setTabResizeMode(int)}.
	 */
	@Test
	public void setWidthCombination1()
	{
		//render fail!
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setBoldActiveTab(true);
		tp.setShowGripper(true);
		tp.setShowCloseButtonOnTab(true);
		tp.setTabResizeMode(XdevTabbedPane2.RESIZE_MODE_FIXED);
		
		Assert.assertTrue(tp.isBoldActiveTab());
		Assert.assertTrue(tp.isShowCloseButtonOnTab());
		Assert.assertTrue(tp.isShowGripper());
		Assert.assertEquals(XdevTabbedPane2.RESIZE_MODE_FIXED, tp.getTabResizeMode());
		
	}
	
	/**
	 * {@link XdevTabbedPane2##setBoldActiveTab(boolean)}.
	 * {@link XdevTabbedPane2##setShowGripper(boolean)}.
	 * {@link XdevTabbedPane2##setShowCloseButtonOnTab(boolean)}.
	 * {@link XdevTabbedPane2##setTabResizeMode(int)}.
	 */
	@Test
	public void setWidthCombination2()
	{
		//render fail!
		final XdevTabbedPane2 tp = new XdevTabbedPane2();
		tp.setBoldActiveTab(true);
		tp.setShowGripper(true);
		tp.setShowCloseButtonOnTab(true);
		tp.setTabResizeMode(XdevTabbedPane2.RESIZE_MODE_FIT);
		
		Assert.assertTrue(tp.isBoldActiveTab());
		Assert.assertTrue(tp.isShowCloseButtonOnTab());
		Assert.assertTrue(tp.isShowGripper());
		Assert.assertEquals(XdevTabbedPane2.RESIZE_MODE_FIT, tp.getTabResizeMode());
		
	}
}
