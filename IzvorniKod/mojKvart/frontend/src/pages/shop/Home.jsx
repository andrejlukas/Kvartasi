import React, { useEffect, useState } from "react";
import { Navbar } from '../../components/ShopNavbar';
import "../../styles/Home.css";
import "bootstrap/dist/css/bootstrap.min.css";

export function ShopHome() {
    const [shopEmail, setShopEmail] = useState(null);
    const [shopId, setShopId] = useState(null);
    const [products, setProducts] = useState([]);
    const [error, setError] = useState("");
  
    useEffect(() => {
        const token = localStorage.getItem('token');
        const options = {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ "oneLiner": token }),
          };
        
        fetch('/api/tokens/claims', options)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Could not extract claims');
                }
                return response.json();
            })
            .then(data => {
                setShopEmail(data.email);
            })
            .catch(error => setError(error.message));
    }, []);

    useEffect(() => {
        if (!shopEmail) return;

        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }
        
        fetch(`/api/trgovinas/${shopEmail}`, options)
            .then((response) => {
                if (!response.ok) {
                    return response.text().then(text => {throw new Error(text)});
                }
                return response.json();
            })
            .then((data) => {
                setShopId(data.trgovinaId);
            })
            .catch((error) => {
                setError(error.message);
            });
    }, [shopEmail]);

    useEffect(() => {
        if (!shopId) return;

        const token = localStorage.getItem('token');
        const options = {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        }

        fetch(`/api/proizvods/fromTrgovina/${shopId}`, options)
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
    }, [shopId]);

    return (
        <div id="vani2">
            <Navbar />
            {error && <p>{error}</p>}
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