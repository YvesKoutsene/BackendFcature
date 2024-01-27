package glsi.example.backendfacture.controller;

import glsi.example.backendfacture.model.ProductModel;
import glsi.example.backendfacture.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Afficher la liste des produits
    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProduits() {
        List<ProductModel> produits = productService.getAllProduits();
        return new ResponseEntity<>(produits, HttpStatus.OK);
    }

    //Affciher un produit
    @GetMapping("/{productId}")
    public ResponseEntity<ProductModel> getProduitById(@PathVariable int productId) {
        ProductModel produit = productService.getProduitById(productId);
        if (produit != null) {
            return new ResponseEntity<>(produit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Créer un produit
    @PostMapping
    public ResponseEntity<ProductModel> createProduit(@RequestBody ProductModel product) {
        ProductModel createdProduct = productService.createProduit(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    //Mettre à jour un produit
    @PutMapping("/{productId}")
    public ResponseEntity<ProductModel> updateProduit(@PathVariable int productId, @RequestBody ProductModel updatedProduct) {
        ProductModel product = productService.updateProduit(productId, updatedProduct);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //Supprimer un produit
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduit(@PathVariable int productId) {
        productService.deleteProduit(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
