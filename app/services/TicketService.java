package services;

import java.util.Collection;
import java.util.List;

import models.PriceList;
import models.PriceList.PriceCategory;
import models.Seat;
import models.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import play.Logger;
import play.Logger.ALogger;
import repository.PriceListRepository;
import repository.TicketRepository;

import com.google.common.collect.Lists;

/**
 * 
 * @author Mark Wigmans
 * 
 */
@Service
public class TicketService {

    private final ALogger logger = Logger.of(getClass());

    private final TicketRepository ticketRepository;
    private final PriceListRepository priceListRepository;

    @Autowired
    public TicketService(final TicketRepository repository, final PriceListRepository priceListRepository) {
        super();
        this.ticketRepository = repository;
        this.priceListRepository = priceListRepository;
    }

    public int count() {
        return ticketRepository.count();
    }

    public int count(final String sid) {
        return ticketRepository.count(sid);
    }

    public Collection<Ticket> list(final String sid, final int index, final int size) {
        return ticketRepository.list(sid, index, size);
    }

    /**
     * Process the bought seats and return tickets for it.
     */
    public List<Ticket> bought(final String sid, final String requestId, final List<Seat> seats,
            final List<PriceCategory> categories) {
        Assert.isTrue(seats.size() == categories.size());

        final List<Ticket> result = Lists.newArrayListWithExpectedSize(seats.size());
        for (int i = 0; i < seats.size(); i++) {
            final Seat seat = seats.get(i);
            final PriceCategory category = categories.get(i);
            result.add(new Ticket(requestId, seat, category, getPrice(seat.priceList, category)));
        }

        ticketRepository.add(sid, result);

        return result;
    }

    Integer getPrice(final String priceListId, final PriceCategory category) {
        final PriceList priceList = priceListRepository.get(priceListId);
        if (priceList != null) {
            return priceList.getPrice(category);
        }
        logger.error("Unknown pricelist ID: {}", priceListId);
        return null;
    }
}