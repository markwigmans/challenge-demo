package services;

import java.math.BigDecimal;
import java.util.List;

import models.PriceCategory;
import models.PriceList;
import models.Seat;
import models.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import play.Logger;
import repository.PriceListRepository;
import repository.TicketRepository;

import com.google.common.collect.Lists;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PriceListRepository priceListRepository;

    @Autowired
    public TicketService(final TicketRepository repository, final PriceListRepository priceListRepository) {
        super();
        this.ticketRepository = repository;
        this.priceListRepository = priceListRepository;
    }

    public int count(final String sid) {
        return ticketRepository.count(sid);
    }

    public List<Ticket> list(final String sid, final int index, final int size) {
        return ticketRepository.list(sid, index, size);
    }

    public List<Ticket> buy(final List<Seat> seats, final int kid, final int adult, final int senior) {
        final List<Ticket> result = Lists.newArrayListWithExpectedSize(seats.size());
        final List<Seat> kidSeats = seats.subList(0, kid);
        final List<Seat> adultSeats = seats.subList(kid, kid + adult);
        final List<Seat> seniorSeats = seats.subList(kid + adult, kid + adult + senior);

        result.addAll(buy(kidSeats, PriceCategory.KID));
        result.addAll(buy(adultSeats, PriceCategory.ADULT));
        result.addAll(buy(seniorSeats, PriceCategory.SENIOR));
        return result;
    }

    List<Ticket> buy(final List<Seat> seats, final PriceCategory category) {
        Logger.debug("seats:" + seats);
        final List<Ticket> result = Lists.newArrayListWithExpectedSize(seats.size());
        for (final Seat seat : seats) {
            result.add(new Ticket(seat, getPrice(seat.priceList, category)));
        }
        return result;
    }

    BigDecimal getPrice(final String priceListId, final PriceCategory category) {
        final PriceList priceList = priceListRepository.get(priceListId);
        if (priceList != null) {
            return priceList.getPrice(category);
        }
        Logger.error("Unknown pricelist ID: " + priceListId);
        return null;
    }
}