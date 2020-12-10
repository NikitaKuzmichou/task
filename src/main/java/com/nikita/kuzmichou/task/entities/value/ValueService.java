package com.nikita.kuzmichou.task.entities.value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ValueService {
    @Autowired
    private ValueRepository repository;

    @Transactional(readOnly = true)
    public Optional<Value> getValue(final String name) {
        return this.repository.findById(name);
    }

    @Transactional
    public Value saveValue(final Value value) {
        return this.repository.save(value);
    }

    @Transactional
    public Value updateValue(final Value value) {
        return this.repository.save(value);
    }

    @Transactional
    public void deleteValue(final Value value) {
        this.repository.delete(value);
    }

    @Transactional
    public void deleteValue(final String name) {
        this.repository.deleteById(name);
    }

    @Transactional(readOnly = true)
    public List<Value> findAll() {
        return this.repository.findAll();
    }
}
