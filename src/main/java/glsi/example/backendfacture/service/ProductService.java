package glsi.example.backendfacture.service;

import glsi.example.backendfacture.model.ProductModel;
import glsi.example.backendfacture.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Afficher la liste des produits
    @Override
    public List<ProductModel> getAllProduits() {
        return productRepository.findAll();
    }

    //Affciher un produit
    @Override
    public ProductModel getProduitById(int productId) {
        Optional<ProductModel> optionalProduct = productRepository.findById(productId);
        return optionalProduct.orElse(null);
    }

    //Créer un produit
    @Override
    public ProductModel createProduit(ProductModel product) {
        // Ajoutez des validations si nécessaire
        return productRepository.save(product);
    }

    //Mettre à jour un produit
    @Override
    public ProductModel updateProduit(int productId, ProductModel updatedProduct) {
        // Vérifier si le produit existe
        ProductModel existingProduct = getProduitById(productId);
        if (existingProduct == null) {
            // Gérer l'erreur de produit non trouvé
            throw new IllegalArgumentException("Produit non trouvé avec l'identifiant : " + productId);
        }

        // Mettre à jour les informations du produit
        existingProduct.setNameProduct(updatedProduct.getNameProduct());
        existingProduct.setPriceUnit(updatedProduct.getPriceUnit());

        // Enregistrer les modifications
        return productRepository.save(existingProduct);
    }

    //Supprimer un produit
    @Override
    public void deleteProduit(int productId) {
        // Vérifier si le produit existe
        ProductModel existingProduct = getProduitById(productId);
        if (existingProduct == null) {
            // Gérer l'erreur de produit non trouvé
            throw new IllegalArgumentException("Produit non trouvé avec l'identifiant : " + productId);
        }

        // Supprimer le produit
        productRepository.deleteById(productId);
    }


}
