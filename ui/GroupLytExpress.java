package hvcs.ui;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;

public abstract class GroupLytExpress
{
	
	protected final GroupLayout layout;
	
	public GroupLytExpress( final Container container )
	{
		this( container, true );
	}
	
	public GroupLytExpress( final Container container,
			final boolean containerGaps )
	{
		this( container, containerGaps, true );
	}
	
	public GroupLytExpress( Container container,
			final boolean containerGaps, final boolean gaps )
	{
		if ( container instanceof RootPaneContainer )
		{
			container = ( (RootPaneContainer) container ).getContentPane();
		}
		
		layout = new GroupLayout( container );
		container.setLayout( layout );
		
		layout.setAutoCreateContainerGaps( containerGaps );
		layout.setAutoCreateGaps( gaps );
		
		layout.setHorizontalGroup( horiz() );
		layout.setVerticalGroup( vert() );
	}
	
	protected abstract Group horiz();
	
	protected abstract Group vert();
	
	protected Group sequential( final Object... members )
	{
		return sequentialAdd(
				layout.createSequentialGroup(), Arrays.asList( members ) );
	}
	
	private Group sequentialAdd(
			final SequentialGroup g, final Iterable<?> objs )
	{
		for ( final Object o : objs )
		{
			if ( o instanceof Component )
			{
				g.addComponent( (Component) o );
			}
			else if ( o instanceof Group )
			{
				g.addGroup( (Group) o );
			}
			else if ( o instanceof Gap )
			{
				( (Gap) o ).addTo( g );
			}
			else if ( o instanceof Object[] )
			{
				sequentialAdd( g, Arrays.asList( (Object[]) o ) );
			}
			else if ( o instanceof Iterable<?> )
			{
				sequentialAdd( g, (Iterable<?>) o );
			}
			else
			{
				throw new IllegalArgumentException( "" + o + " is not a "
						+ "Component, Group, gap, reference array, or Iterable" );
			}
		}
		
		return g;
	}
	
	public static final Alignment
			Leading = Alignment.LEADING, Centered = Alignment.CENTER,
			OnBaseline = Alignment.BASELINE, Trailing = Alignment.TRAILING;
	
	protected Group prlLeading( final Object... members )
	{
		return parallel( Leading, members );
	}
	
	protected Group prlCentered( final Object... members )
	{
		return parallel( Centered, members );
	}
	
	protected Group prlOnBaseline( final Object... members )
	{
		return parallel( OnBaseline, members );
	}
	
	protected Group prlFixed( final Object... members )
	{
		return parallel( Centered, false, members );
	}
	
	protected Group prlFixed( final Alignment align, final Object... members )
	{
		return parallel( align, false, members );
	}
	
	protected Group prlTrailing( final Object... members )
	{
		return parallel( Trailing, members );
	}
	
	protected Group parallel( final Alignment align, final Object... members )
	{
		return parallelAdd( layout.createParallelGroup( align ),
				Arrays.asList( members ), align );
	}
	
	protected Group parallel( final Alignment align, final boolean resizable,
			final Object... members )
	{
		return parallelAdd( layout.createParallelGroup( align, resizable ),
				Arrays.asList( members ), align );
	}
	
	private Group parallelAdd( final ParallelGroup g, final Iterable<?> objs,
			final Alignment defaultAlign )
	{
		final Iterator<?> iter = objs.iterator();
		if ( iter.hasNext() )
		{
			Object next = iter.next();
			
			do
			{
				if ( next instanceof Alignment )
				{
					throw new IllegalArgumentException( "Alignment argument \"" +
							next + "\" without preceding target" );
				}
				
				final Object current = next;
				final Alignment align;
				final boolean hasAlignArg;
				
				if ( iter.hasNext() )
				{
					next = iter.next();
					if ( hasAlignArg = next instanceof Alignment )
					{
						align = (Alignment) next;
						
						// skip checking alignment object in next iteration
						next = iter.hasNext() ? iter.next() : null;
					}
					else
					{
						align = defaultAlign;
					}
				}
				else
				{
					next = null;
					
					hasAlignArg = false;
					align = defaultAlign;
				}
				
				if ( current instanceof Component )
				{
					g.addComponent( (Component) current, align );
				}
				else if ( current instanceof Group )
				{
					g.addGroup( align, (Group) current );
				}
				else if ( current instanceof Gap )
				{
					( (Gap) current ).addTo( g );
					if ( hasAlignArg )
					{
						throw new IllegalArgumentException(
								"Alignment following gap (gaps can't be aligned)" );
					}
				}
				else if ( current instanceof Object[] )
				{
					parallelAdd( g, Arrays.asList( (Object[]) current ), align );
				}
				else if ( current instanceof Iterable<?> )
				{
					parallelAdd( g, (Iterable<?>) current, align );
				}
				else
				{
					throw new IllegalArgumentException( "" + current + " is not " +
							"a Component, Group, gap, Alignment, " +
							"reference array, or Iterable" );
				}
			}
			while ( next != null );
		}
		
		return g;
	}
	
	protected Gap gap( final int size )
	{
		return new Gap( size );
	}
	
	protected FlexGap gap( final int min, final int pref, final int max )
	{
		return new FlexGap( min, pref, max );
	}
	
	private static class Gap
	{
		
		protected final int size;
		
		public Gap( final int size )
		{
			this.size = size;
		}
		
		public void addTo( final Group g )
		{
			g.addGap( size );
		}
		
	}
	
	private static class FlexGap extends Gap
	{
		
		protected final int min, max;
		
		public FlexGap( final int min, final int pref, final int max )
		{
			super( pref );
			this.min = min;
			this.max = max;
		}
		
		@Override
		public void addTo( final Group g )
		{
			g.addGap( min, size, max );
		}
		
	}
	
}