package xdev.ui.charts;

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


/**
 * Specifies a groupable Chart.
 * 
 * 
 * @author XDEV Software jwill
 * @since 4.0
 */
public interface GroupableChart
{
	static final double	DEFAULT_BAR_GAP_PROPORTION			= 0.8;
	static final double	DEFAULT_BAR_GROUP_GAP_PROPORTION	= 0.2;
	
	
	/**
	 * Groups the charts bars.
	 * 
	 * @param barsGrouped
	 *            indicates if the charts should be grouped or not.
	 */
	void setBarsGrouped(boolean barsGrouped);
	
	
	/**
	 * Returns the charts grouped state.
	 * 
	 * @return indicates if the charts are grouped or not.
	 */
	boolean isBarsGrouped();
	
	
	/**
	 * Specifies the gap (in pixels) between groups of bars in a grouped bar
	 * chart.
	 * 
	 * @param barGroupGap
	 *            the group gap to set.
	 */
	void setBarGroupGap(int barGroupGap);
	
	
	/**
	 * Specifies the pixel gap between bars in a bar chart.
	 * 
	 * @param barGap
	 *            the bar gap to set.
	 */
	void setBarGap(int barGap);
	
	
	/**
	 * Returns the bar gap.
	 * 
	 * @return the set bar gap.
	 */
	int getBarGap();
	
	
	/**
	 * Specify the size of the group gap as a proportion of the space available
	 * to the group.
	 * 
	 * @param barGroupGapProportion
	 *            the bar group gap proportion to set.
	 */
	void setBarGroupGapProportion(Double barGroupGapProportion);
}
