import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Navbar } from '../components/Navbar';
import "../styles/Shop.css";
import "bootstrap/dist/css/bootstrap.min.css";

export default function Shop() {
  const { id } = useParams();
  const [shop, setShop] = useState(null);
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
    if (id) {
      fetch(`/api/trgovinas/${id}`, options)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Neuspjesno dohvacanje trgovine.");
          }
          return response.json();
        })
        .then((data) => {
          setShop(data);
        })
        .catch((error) => {
          setError(error.message);
        });
    }
  }, [id]);

  useEffect(() => {
    if (id) {
      const token = localStorage.getItem('token');
      const options = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    }
      fetch(`/api/proizvods/fromTrgovina/${id}`, options)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Neuspjesno dohvacanje proizvoda.");
          }
          return response.json();
        })
        .then((data) => {
          setProducts(data);
        })
        .catch((error) => {
          setError(error.message);
        });
    }
  }, [id]);

  return (
    <div>
      <Navbar />
      <div className="content-wrapper">
        <div className="container mt-4">
          {error ? (
            <p className="text-danger">{error}</p>
          ) : !shop ? (
            <p>Loading shop details...</p>
          ) : (
            <div className="shop-product-row">
              <div id="shops" className="shop-info">
                <img 
                  src={shop.trgovinaSlika} 
                  alt={shop.trgovinaNaziv} 
                  className="img-fluid mb-3 shop-image"
                />
                <div id="pomoc">
                <h2 id="nazivTrgovine">{shop.trgovinaNaziv || "N/A"}</h2>
                <p id="opis"><strong></strong> {shop.trgovinaOpis || "Nije dostupan"}</p>
                </div>
                <div className="divider"></div>

                <p><strong>Adresa:</strong> {shop.trgovinaLokacija || "Nije specifirano"}</p>
                <p><strong>Radno vrijeme: </strong> 
                  {shop.trgovinaRadnoVrijemeOd && shop.trgovinaRadnoVrijemeDo 
                    ? `${shop.trgovinaRadnoVrijemeOd} - ${shop.trgovinaRadnoVrijemeDo}`
                    : "Nije specifirano"}
                </p>
                <p><strong>Kategorija: </strong> {shop.trgovinaKategorija || "Nije specifirano"}</p>
                <p><strong>Email: </strong> {shop.trgovinaEmail || "Nije dostupan"}</p>
              </div>

              <div id="products" className="product-section">
                <div className="row">
                  {products.length > 0 ? (
                    products.map((product) => (
                      <div key={product.proizvodId} className="col-md-6 mb-3">
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
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
