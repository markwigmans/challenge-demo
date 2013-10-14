package services;

import java.util.Collection;
import java.util.List;

import models.Stadium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.StadiumRepository;
import repository.StadiumRepository.BlockCount;

/**
 * 
 * @author Mark Wigmans
 * 
 */
@Service
public class StadiumService {

    private final StadiumRepository repository;

    @Autowired
    public StadiumService(final StadiumRepository repository) {
        super();
        this.repository = repository;
    }

    public void reset() {
        repository.reset();
    }

    public Collection<Stadium> getAll() {
        return repository.getAll();
    }

    public Stadium get(final String id) {
        return repository.get(id);
    }

    public void add(final Stadium stadium) {
        repository.add(stadium);
    }

    public List<BlockCount> available(final String id) {
        return repository.available(id);
    }
}