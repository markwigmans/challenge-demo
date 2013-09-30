package repository;

import java.util.Collection;
import java.util.List;

import org.springframework.util.Assert;

import models.Ticket;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class TicketRepository {

    private final Multimap<String, Ticket> tickets = ArrayListMultimap.create();

    public synchronized void add(final String sid, final Ticket ticket) {
        Assert.hasLength(sid, "stadium ID must have text");
        Assert.notNull(ticket);
        tickets.put(sid, ticket);
    }

    public int count(final String sid) {
        Assert.hasLength(sid, "stadium ID must have text");
        return tickets.get(sid).size();
    }

    public List<Ticket> list(final String sid, final int index, final int size) {
        Assert.hasLength(sid, "stadium ID must have text");
        final Collection<Ticket> collection = tickets.get(sid);
        return Lists.newArrayList(Iterables.partition(collection, size)).get(index);
    }
}