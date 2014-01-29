package de.fhhof.universe.client.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import de.fhhof.universe.shared.data.proto.ShipConfig;
import de.fhhof.universe.shared.data.proto.util.ConfigurationManager;

/**
 * Modell für Dropdown-Liste von Schiffen.
 * 
 * @author Daniela Geilert
 *
 */
public class ShipBoxModel implements ComboBoxModel
{
	private final List<ShipConfig> configs;
	private int selectionIndex = 0;
	private List<ListDataListener> listeners;
	
	/**
	 * Erzeugt ein ComboBoxModel mit der Schiffsauswahl.
	 * Wirft eine NullPointerException, wenn die Konfigurationsloste null ist.
	 * 
	 * @param configs anzuzeigende Konfigurationen
	 */
	public ShipBoxModel(List<Short> confs)
	{
		if(confs == null)
		{
			throw new NullPointerException("Keine Konfigurationen erhalten");
		}
		
		configs = new ArrayList<ShipConfig>(confs.size());
		
		ConfigurationManager cm = ConfigurationManager.getInstance();
		
		for(Short uid : confs)
		{
			ShipConfig c = cm.getConfiguration(ShipConfig.class, uid);
			if(c != null)
			{
				configs.add(c);
			}
		}
		
		listeners = new ArrayList<ListDataListener>();
	}
	
	@Override
	public int getSize()
	{
		return configs.size();
	}

	@Override
	public ShipConfig getElementAt(int index)
	{
		return configs.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l)
	{
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l)
	{
		listeners.remove(l);
	}

	@Override
	public void setSelectedItem(Object anItem)
	{
		selectionIndex = configs.indexOf(anItem);
		
		if(selectionIndex < 0)
		{
			selectionIndex = 0;
		}
		
		ListDataEvent event = new ListDataEvent(this,
				ListDataEvent.CONTENTS_CHANGED, 0, configs.size() - 1);
		for(ListDataListener ldl : listeners)
		{
			ldl.contentsChanged(event);
		}
	}

	@Override
	public ShipConfig getSelectedItem()
	{
		ShipConfig config = null;
		
		if(selectionIndex >= 0 && selectionIndex < configs.size())
		{
			config = configs.get(selectionIndex);
		}
		
		return config;
	}

}
