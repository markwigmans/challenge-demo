package repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import models.Block;
import models.Seat;
import models.Stadium;

import org.springframework.util.Assert;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * Storage of stadiums.
 * 
 * @author Mark Wigmans
 * 
 */
public class StadiumRepository {

    private final Map<String, Stadium> stadiums = Maps.newHashMap();
    private final Map<String, Multimap<String, Seat>> seatsMap = Maps.newHashMap();

    public Collection<Stadium> getAll() {
        return Collections.unmodifiableCollection(stadiums.values());
    }

    public Stadium get(final String sid) {
        Assert.hasLength(sid, "stadium ID must have text");
        return stadiums.get(sid);
    }

    public int available(final String sid, final String bid) {
        Assert.hasLength(sid, "stadium ID must have text");
        Assert.hasLength(bid, "block ID must have text");
        return getSeats(sid, bid).size();
    }

    public List<BlockCount> available(final String sid) {
        Assert.hasLength(sid, "stadium ID must have text");
        final List<BlockCount> result = Lists.newArrayList();
        final Multimap<String, Seat> blocks = seatsMap.get(sid);
        for (final String blockId : blocks.keySet()) {
            result.add(new BlockCount(blockId, blocks.get(blockId).size()));
        }
        return result;
    }

    public synchronized void add(final Stadium stadium) {
        Assert.notNull(stadium);
        stadiums.put(stadium.id, stadium);
        final Multimap<String, Seat> map = ArrayListMultimap.create();
        seatsMap.put(stadium.id, map);
    }

    public synchronized void add(final String sid, final Block block) {
        Assert.hasLength(sid, "stadium ID must have text");
        Assert.notNull(block);
        Assert.isTrue(getSeats(sid, block.id).isEmpty(), "Cannot add a block a second time");
        final List<Seat> seatList = Lists.newArrayListWithExpectedSize(block.rows * block.seats);
        for (int row = 1; row <= block.rows; row++) {
            for (int seat = 1; seat <= block.seats; seat++) {
                seatList.add(new Seat(row, seat, true, block.defaultPrice));
            }
        }
        seatsMap.get(sid).putAll(block.id, seatList);
    }

    public synchronized void remove(final String sid, final String bid, final Seat removedSeat) {
        Assert.hasLength(sid, "stadium ID must have text");
        Assert.hasLength(bid, "block ID must have text");
        Assert.notNull(removedSeat);
        final Collection<Seat> collection = getSeats(sid, bid);
        final Collection<Seat> updated = Lists.newArrayListWithExpectedSize(collection.size());
        for (final Seat seat : collection) {
            if (seat.row == removedSeat.row && seat.seat == removedSeat.seat) {
                // found and skip it
            } else {
                updated.add(seat);
            }
        }
        seatsMap.get(sid).replaceValues(bid, updated);
    }

    public synchronized void update(final String sid, final String bid, final Seat updatedSeat) {
        Assert.hasLength(sid, "stadium ID must have text");
        Assert.hasLength(bid, "block ID must have text");
        Assert.notNull(updatedSeat);
        final Collection<Seat> collection = getSeats(sid, bid);
        final Collection<Seat> updated = Lists.newArrayListWithExpectedSize(collection.size());
        for (final Seat seat : collection) {
            if (seat.row == updatedSeat.row && seat.seat == updatedSeat.seat) {
                // found
                updated.add(updatedSeat);
            } else {
                updated.add(seat);
            }
        }
        seatsMap.get(sid).replaceValues(bid, updated);
    }

    public int count() {
        return stadiums.size();
    }

    public synchronized void reset() {
        stadiums.clear();
        seatsMap.clear();
    }

    public synchronized List<Seat> buy(final String sid, final String bid, final int needed) {
        final Collection<Seat> seats = getSeats(sid, bid);
        if (seats.size() >= needed) {
            // enough seats available
            final List<Seat> seatList = Lists.newArrayList(seats);
            final List<Seat> result = seatList.subList(0, needed);
            seatsMap.get(sid).replaceValues(bid, seatList.subList(needed, seats.size()));
            return result;
        } else {
            // not enough seats available
            return null;
        }
    }

    Collection<Seat> getSeats(final String sid, final String bid) {
        final Multimap<String, Seat> blocks = seatsMap.get(sid);
        if (blocks == null) {
            throw new IllegalArgumentException(String.format("Stadium:[%s] is unknown", sid));
        }
        final Collection<Seat> seats = blocks.get(bid);
        if (seats == null) {
            throw new IllegalArgumentException(String.format("Stadium:[%s][%s] is unknown", sid, bid));
        }
        return seats;
    }

    public static class BlockCount {
        public final String id;
        public final int count;

        BlockCount(final String id, final int count) {
            super();
            this.id = id;
            this.count = count;
        }
    }
}
