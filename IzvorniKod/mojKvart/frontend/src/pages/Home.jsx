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
    <div id="vani">
      <Navbar />
      <div className="content-wrapper">
        <div className="container mt-4">
          {error ? (
            <p className="text-danger">{error}</p>
          ) : (
            <div id="products" className="product-section">
              <div className="row">
                {products.length > 0 ? (
                  products.map((product) => (
                    <div key={product.proizvodId} className="col-md-4 mb-3">
                      <div className="card product-card">
                        <img 
                          src={product.proizvodSlika} 
                          className="card-img-top" 
                          alt={product.proizvod_naziv} 
                        />
                        <div className="card-body">
                          <h5 className="card-title">{product.proizvodNaziv}</h5>
                          <p className="card-text">{product.proizvodOpis}</p>
                          <div id="gumbcijena">
                            <p className="price">€{product.proizvodCijena}</p>
                            <button className="add-to-cart-btn">Dodaj u košaricu</button>
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
          )}
        </div>
      </div>
    </div>
  );
}
