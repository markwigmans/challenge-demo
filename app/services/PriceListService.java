package services;

import java.util.List;

import models.PriceList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.PriceListRepository;

@Service
public class PriceListService {

    private final PriceListRepository repository;

    @Autowired
    public PriceListService(final PriceListRepository repository) {
        super();
        this.repository = repository;
    }

    public void reset() {
        repository.reset();
    }

    public List<PriceList> getAll() {
        return repository.getAll();
    }

    public PriceList get(final String id) {
        return repository.get(id);
    }

    public PriceList add(final PriceList priceList) {
        return repository.add(priceList);
    }
}