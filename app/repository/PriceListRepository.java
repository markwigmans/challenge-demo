package repository;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;

import models.PriceList;

import com.google.common.collect.Maps;

/**
 * Storage of price lists.
 * 
 * @author Mark Wigmans
 * 
 */
public class PriceListRepository {

    private final ConcurrentMap<String, PriceList> priceLists = Maps.newConcurrentMap();

    public Collection<PriceList> getAll() {
        return Collections.unmodifiableCollection(priceLists.values());
    }

    public PriceList get(final String id) {
        return priceLists.get(id);
    }

    public PriceList add(final PriceList stadium) {
        return priceLists.put(stadium.id, stadium);
    }

    public int count() {
        return priceLists.size();
    }

    public void reset() {
        priceLists.clear();
    }
}
