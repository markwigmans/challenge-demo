package services;

import java.util.List;

import models.Block;
import models.Seat;
import models.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.StadiumRepository;

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
        final Seat seat = new Seat(rowNr, seatNr, priceList);
        stadiumRepository.update(sid, bid, seat);
        return seat;
    }

    public Seat remove(final String sid, final String bid, final int rowNr, final int seatNr) {
        final Seat seat = new Seat(rowNr, seatNr, null);
        stadiumRepository.remove(sid, bid, seat);
        return seat;
    }

    public int available(final String sid, final String bid) {
        return stadiumRepository.available(sid, bid);
    }

    public List<Ticket> buy(final String sid, final String bid, final int kid, final int adult, final int senior) {
        final int needed = kid + adult + senior;
        final List<Seat> seats = stadiumRepository.buy(sid, bid, needed);
        if (seats != null) {
            return ticketService.buy(seats, kid, adult, senior);
        } else {
            // buying the given number of tickets was not successful.
            return null;
        }
    }
}