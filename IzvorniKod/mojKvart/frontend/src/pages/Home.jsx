import React, { useEffect, useState } from "react";
import { Navbar } from '../components/Navbar';
import "../styles/home.css";
import "bootstrap/dist/css/bootstrap.min.css";

export function Home() {
  const [products, setProducts] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const options = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    }
    fetch('/api/proizvods', options)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch products.");
        }
        return response.json();
      })
      .then((data) => {
        setProducts(data);
      })
      .catch((error) => {
        setError(error.message);
      });
  }, []);

  return (
    <div id="vani2">
  <Navbar />
  <div id="products-alt" className="product-section-alt">
    <div className="row">
        {products.length > 0 ? (
            products.map((product) => (
                <div id="productsbox" key={product.proizvodId} className="col-lg-4 col-md-6 col-12 mb-3-alt">
                    <div className="card-alt product-card-alt">
                        <img 
                          src={product.proizvodSlika} 
                          className="card-img-top-alt" 
                          alt={product.proizvod_naziv} 
                        />
                        <div className="card-body-alt">
                            <h5 className="card-title-alt">{product.proizvodNaziv}</h5>
                            <p className="card-text-alt">{product.proizvodOpis}</p>
                            <div id="gumbcijena-alt">
                                <p className="price-alt">€{product.proizvodCijena}</p>
                                <button className="add-to-cart-btn-alt">Dodaj u košaricu</button>
                            </div>
                        </div>
                    </div>
                </div>
            ))
        ) : (
            <p>Nisu dostupni proizvodi.</p>
        )}
    </div>
</div>
</div>

  );
}
