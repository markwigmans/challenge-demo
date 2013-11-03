package services;

import java.util.Collections;
import java.util.List;

import models.Block;
import models.PriceList.PriceCategory;
import models.Seat;
import models.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.StadiumRepository;

/**
 * 
 * @author Mark Wigmans
 * 
 */
@Service
public class BlockService {

    private final StadiumRepository stadiumRepository;
    private final TicketService ticketService;

    @Autowired
    public BlockService(final StadiumRepository stadiumRepository, final TicketService ticketService) {
        super();
        this.stadiumRepository = stadiumRepository;
        this.ticketService = ticketService;
    }

    public void add(final String sid, final Block block) {
        stadiumRepository.add(sid, block);
    }

    public Seat update(final String sid, final String bid, final int rowNr, final int seatNr, final String priceList) {
        final Seat seat = new Seat(rowNr, seatNr, true, priceList);
        stadiumRepository.update(sid, bid, seat);
        return seat;
    }

    public Seat remove(final String sid, final String bid, final int rowNr, final int seatNr) {
        final Seat seat = new Seat(rowNr, seatNr, false, null);
        stadiumRepository.remove(sid, bid, seat);
        return seat;
    }

    public int available(final String sid, final String bid) {
        return stadiumRepository.available(sid, bid);
    }

    public List<Ticket> buy(final String sid, final String bid, final String requestId, final List<PriceCategory> categories) {
        final int needed = categories.size();
        final List<Seat> seats = stadiumRepository.buy(sid, bid, needed);
        if (seats != null) {
            return ticketService.bought(sid, requestId, seats, categories);
        } else {
            // buying the given number of tickets was not successful, so return an empty list
            return Collections.emptyList();
        }
    }
}