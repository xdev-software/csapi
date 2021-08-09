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


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.SwingConstants;

import com.jidesoft.pane.CollapsiblePane;


/**
 * <p>
 * {@link XdevCollapsiblePaneContainer} is a grouping container for
 * {@link XdevCollapsiblePane}.
 * </p>
 * 
 * <p>
 * Usually all collapsible panes are aligned to the top. To add the
 * CollapsiblePane, the Container.add(java.awt.Component) method is used. To
 * define the constraints of the {@link XdevCollapsiblePane} use
 * {@link XdevCollapsiblePane#setFlexible(boolean)} and
 * {@link XdevCollapsiblePane#setFlexibleWeight(float)}.
 * </p>
 * 
 * <code>
 * CollapsiblePanes pane = new CollapsiblePanes();<br>
 * CollapsiblePane
 * fileFolderTaskPane = createFileFolderTaskPane();<br>
 * fileFolderTaskPane.setFlexible(true);<br> CollapsiblePane
 * otherPlacesPane = createOtherPlacesPane(); <br>pane.add(fileFolderTaskPane); <br>
 * pane.add(otherPlacesPane);
 * </code>
 * 
 * @author XDEV Software jwill, fh
 * @since 4.0
 * 
 */
public class XdevCollapsiblePaneContainer extends XComponent implements XdevFocusCycleComponent,
		SwingConstants
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7324462844058468747L;
	
	/**
	 * the tab index.
	 */
	private int					tabIndex			= -1;
	private int					gap					= 0;
	private int					orientation			= VERTICAL;
	
	
	/**
	 * Creates a new {@link XdevCollapsiblePaneContainer} using a special
	 * {@link BoxLayout}.
	 */
	public XdevCollapsiblePaneContainer()
	{
		super(null);
		setLayout(new Layout());
	}
	
	
	/**
	 * Sets the gap between two collapsible panes.
	 * 
	 * @param gap
	 *            the new gap
	 */
	@BeanProperty(category = DefaultBeanCategories.OBJECT)
	public void setGap(int gap)
	{
		this.gap = gap;
	}
	
	
	/**
	 * Gets the gap between two collapsible panes.
	 * 
	 * @return the gap between two collapsible panes.
	 */
	public int getGap()
	{
		return gap;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addImpl(Component comp, Object constraints, int index)
	{
		if(constraints == null && comp instanceof XdevCollapsiblePane)
		{
			XdevCollapsiblePane cp = (XdevCollapsiblePane)comp;
			constraints = cp.isFlexible() ? cp.getFlexibleWeight() : "FIX";
		}
		
		super.addImpl(comp,constraints,index);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTabIndex()
	{
		return this.tabIndex;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTabIndex(int tabIndex)
	{
		this.tabIndex = tabIndex;
	}
	
	
	/**
	 * Sets the orientation of the container. The orientation must have either
	 * the value <code>HORIZONTAL</code> or <code>VERTICAL</code>. If
	 * <code>orientation</code> is an invalid value, an exception will be
	 * thrown.
	 * 
	 * @param orientation
	 *            the new orientation -- either <code>HORIZONTAL</code> or
	 *            <code>VERTICAL</code>
	 * @exception IllegalArgumentException
	 *                if orientation is neither <code>HORIZONTAL</code> nor
	 *                <code>VERTICAL</code>
	 * @see #getOrientation
	 * @beaninfo description: The current orientation of the container bound:
	 *           true preferred: true enum: HORIZONTAL SwingConstants.HORIZONTAL
	 *           VERTICAL SwingConstants.VERTICAL
	 */
	@BeanProperty(category = DefaultBeanCategories.OBJECT)
	public void setOrientation(int orientation)
	{
		switch(orientation)
		{
			case VERTICAL:
			case HORIZONTAL:
			break;
			default:
				throw new IllegalArgumentException(
						"orientation must be one of: VERTICAL, HORIZONTAL");
		}
		
		this.orientation = orientation;
	}
	
	
	/**
	 * Returns the current orientation of the container. The value is either
	 * <code>HORIZONTAL</code> or <code>VERTICAL</code>.
	 * 
	 * @return an integer representing the current orientation -- either
	 *         <code>HORIZONTAL</code> or <code>VERTICAL</code>
	 * @see #setOrientation
	 */
	public int getOrientation()
	{
		return orientation;
	}
	
	
	
	private class Layout implements LayoutManager2
	{
		private Map<Component, Object>	constraintsMap	= new LinkedHashMap();
		
		
		@Override
		public void addLayoutComponent(Component comp, Object constraints)
		{
			if(!("FIX".equals(constraints) || constraints instanceof Number))
			{
				throw new IllegalArgumentException("constraints");
			}
			else if(constraints instanceof Number)
			{
				float weight = ((Number)constraints).floatValue();
				if(weight <= 0.0f || weight > 1.0f)
				{
					throw new IllegalArgumentException(
							"weight out of range, expected: 0 < weight <= 1.0");
				}
			}
			
			constraintsMap.put(comp,constraints);
		}
		
		
		@Override
		public void removeLayoutComponent(Component comp)
		{
			constraintsMap.remove(comp);
		}
		
		
		@Override
		public void layoutContainer(Container parent)
		{
			Dimension size = parent.getSize();
			Insets insets = parent.getInsets();
			Rectangle place = new Rectangle(insets.left,insets.top,size.width - insets.left
					- insets.right,size.height - insets.top - insets.bottom);
			
			int componentCount = 0;
			for(Component cpn : constraintsMap.keySet())
			{
				if(cpn.isVisible())
				{
					componentCount++;
				}
			}
			
			int space = gap * (Math.max(0,componentCount - 1));
			
			switch(orientation)
			{
				case VERTICAL:
				{
					int variableHeight = place.height - space;
					float variableWeightSum = 0.0f;
					
					for(Component cpn : constraintsMap.keySet())
					{
						if(!cpn.isVisible())
						{
							continue;
						}
						
						Object constraints = constraintsMap.get(cpn);
						if("FIX".equals(constraints) || isCollapsed(cpn))
						{
							variableHeight -= cpn.getPreferredSize().height;
						}
						else
						{
							variableWeightSum += (Float)constraints;
						}
					}
					
					int y = place.y;
					for(Component cpn : constraintsMap.keySet())
					{
						if(!cpn.isVisible())
						{
							continue;
						}
						
						Object constraints = constraintsMap.get(cpn);
						if("FIX".equals(constraints) || isCollapsed(cpn))
						{
							cpn.setBounds(place.x,y,place.width,cpn.getPreferredSize().height);
						}
						else
						{
							float weight = (Float)constraints;
							weight /= variableWeightSum;
							cpn.setBounds(place.x,y,place.width,(int)(variableHeight * weight));
						}
						
						y += cpn.getHeight() + gap;
					}
				}
				break;
				
				case HORIZONTAL:
				{
					int variableWidth = place.width - space;
					float variableWidthSum = 0.0f;
					
					for(Component cpn : constraintsMap.keySet())
					{
						if(!cpn.isVisible())
						{
							continue;
						}
						
						Object constraints = constraintsMap.get(cpn);
						if("FIX".equals(constraints) || isCollapsed(cpn))
						{
							variableWidth -= cpn.getPreferredSize().width;
						}
						else
						{
							variableWidthSum += (Float)constraints;
						}
					}
					
					int x = place.x;
					for(Component cpn : constraintsMap.keySet())
					{
						if(!cpn.isVisible())
						{
							continue;
						}
						
						Object constraints = constraintsMap.get(cpn);
						if("FIX".equals(constraints) || isCollapsed(cpn))
						{
							cpn.setBounds(x,place.y,cpn.getPreferredSize().width,place.height);
						}
						else
						{
							float weight = (Float)constraints;
							weight /= variableWidthSum;
							cpn.setBounds(x,place.y,(int)(variableWidth * weight),place.height);
						}
						
						x += cpn.getWidth() + gap;
					}
				}
				break;
			}
		}
		
		
		private boolean isCollapsed(Component cpn)
		{
			if(cpn instanceof CollapsiblePane)
			{
				return ((CollapsiblePane)cpn).isCollapsed();
			}
			return true;
		}
		
		
		@Override
		public void addLayoutComponent(String name, Component comp)
		{
		}
		
		
		@Override
		public Dimension minimumLayoutSize(Container parent)
		{
			Dimension sum = new Dimension();
			
			int componentCount = 0;
			for(Component cpn : constraintsMap.keySet())
			{
				if(cpn.isVisible())
				{
					componentCount++;
				}
			}
			
			if(componentCount > 0)
			{
				switch(orientation)
				{
					case VERTICAL:
					{
						for(Component cpn : constraintsMap.keySet())
						{
							if(!cpn.isVisible())
							{
								continue;
							}
							
							Dimension d = cpn.getMinimumSize();
							sum.width = Math.max(sum.width,d.width);
							sum.height += d.height;
						}
						
						sum.height += (componentCount - 1) * gap;
					}
					break;
					
					case HORIZONTAL:
					{
						for(Component cpn : constraintsMap.keySet())
						{
							if(!cpn.isVisible())
							{
								continue;
							}
							
							Dimension d = cpn.getMinimumSize();
							sum.width += d.width;
							sum.height = Math.max(sum.height,d.height);
						}
						
						sum.width += (componentCount - 1) * gap;
					}
					break;
				}
			}
			
			Insets insets = parent.getInsets();
			sum.width += insets.left + insets.right;
			sum.height += insets.top + insets.bottom;
			
			return sum;
		}
		
		
		@Override
		public Dimension preferredLayoutSize(Container parent)
		{
			Dimension sum = new Dimension();
			
			int componentCount = 0;
			for(Component cpn : constraintsMap.keySet())
			{
				if(cpn.isVisible())
				{
					componentCount++;
				}
			}
			
			if(componentCount > 0)
			{
				switch(orientation)
				{
					case VERTICAL:
					{
						for(Component cpn : constraintsMap.keySet())
						{
							if(!cpn.isVisible())
							{
								continue;
							}
							
							Dimension d = cpn.getPreferredSize();
							sum.width = Math.max(sum.width,d.width);
							sum.height += d.height;
						}
						
						sum.height += (componentCount - 1) * gap;
					}
					break;
					
					case HORIZONTAL:
					{
						for(Component cpn : constraintsMap.keySet())
						{
							if(!cpn.isVisible())
							{
								continue;
							}
							
							Dimension d = cpn.getPreferredSize();
							sum.width += d.width;
							sum.height = Math.max(sum.height,d.height);
						}
						
						sum.width += (componentCount - 1) * gap;
					}
					break;
				}
			}
			
			Insets insets = parent.getInsets();
			sum.width += insets.left + insets.right;
			sum.height += insets.top + insets.bottom;
			
			return sum;
		}
		
		
		@Override
		public Dimension maximumLayoutSize(Container target)
		{
			return null;
		}
		
		
		@Override
		public float getLayoutAlignmentX(Container target)
		{
			return 0.5f;
		}
		
		
		@Override
		public float getLayoutAlignmentY(Container target)
		{
			return 0.5f;
		}
		
		
		@Override
		public void invalidateLayout(Container target)
		{
		}
	}
}
