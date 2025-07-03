package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Product;
import repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository pRepository;

    public List<Product> getAll() {
        return pRepository.findAll();
    }

    public Product getById(int id) {
        return pRepository.findById(id).orElse(null);
    }

    public Product register(Product product) {
        return pRepository.save(product);
    }

    public void delete(int id) {
        pRepository.deleteById(id);;
    }
}
