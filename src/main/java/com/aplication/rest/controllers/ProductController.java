package com.aplication.rest.controllers;

import com.aplication.rest.controllers.dto.MakerDTO;
import com.aplication.rest.controllers.dto.ProductDTO;
import com.aplication.rest.entities.Maker;
import com.aplication.rest.entities.Product;
import com.aplication.rest.persistence.impl.MakerDAOImpl;
import com.aplication.rest.service.IProductService;
import com.aplication.rest.service.impl.MakerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private IProductService productService;
    @Autowired
    private MakerServiceImpl makerServiceImpl;

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Product> productOptional = productService.findById(id);
        if (productOptional.isPresent()){
            Product product = productOptional.get();

            Maker maker = product.getMaker();

            MakerDTO makerDTO = MakerDTO.builder()
                    .id(maker.getId())
                    .name(maker.getName())
                    .build();

            ProductDTO productDTO = ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .maker(makerDTO)
                    .build();
            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<ProductDTO> productList = productService.findAll()
                .stream()
                .map(product -> {
                    Maker maker = product.getMaker();
                    MakerDTO makerDTO = MakerDTO.builder()
                            .id(maker.getId())
                            .name(maker.getName())
                            .build();

                    return ProductDTO.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(product.getPrice())
                            .maker(makerDTO)
                            .build();
                })
                .toList();

        return ResponseEntity.ok(productList);
    }


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody ProductDTO productDTO) throws URISyntaxException {
        if (productDTO.getName() == null || productDTO.getName().isBlank() || productDTO.getPrice()==null || productDTO.getMaker()==null){
            return ResponseEntity.badRequest().build();
        }

        Optional<Maker> makerOptional = makerServiceImpl.findById(productDTO.getMaker().getId());

        if (makerOptional.isPresent()) {
            Maker maker = makerOptional.get();

            Product product = Product.builder()
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .maker(maker)
                    .build();
            productService.save(product);

            return ResponseEntity.created(new URI("/api/product/save")).build();
        }else {
            return ResponseEntity.badRequest().body("Ese id no existe");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        if (productDTO.getName() == null || productDTO.getName().isBlank() || productDTO.getPrice() == null) {
            return ResponseEntity.badRequest().body("El nombre y el precio son campos requeridos.");
        }

        Optional<Product> productOptional = productService.findById(id);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(productDTO.getName());
            product.setPrice(productDTO.getPrice());

            productService.save(product);
            return ResponseEntity.ok("Registro Actualizado");
        }

        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {

        if(id != null){
            productService.deleteById(id);
            return ResponseEntity.ok("Registro Eliminado");
        }
        return ResponseEntity.badRequest().build();
    }
}