import React, { useEffect, useState } from "react";
import { Navbar } from '../../components/Navbar';
import "../../styles/Home.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Link } from 'react-router-dom';

export function Home() {
  const [products, setProducts] = useState([]);
  const [error, setError] = useState("");
  const [trgovinaNames, setTrgovinaNames] = useState({});

  useEffect(() => {
    const token = localStorage.getItem('token');
    const options = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    }
    fetch('/api/proizvods/approved', options)
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw new Error(text);
        }
        return response.json();
      })
      .then((data) => {
        setProducts(data);
        data.forEach(dog => fetchTrgovinaName(dog.trgovina));
      })
      .catch((error) => {
        setError(error.message);
      });

      const fetchTrgovinaName = async (trgovinaId) => {
        try {
           const options3 = {
              method: 'GET',
              headers: {
              'Authorization': `Bearer ${token}`,
              'Content-Type': 'application/json',
              },
           };
           const response = await fetch(`/api/trgovinas/getById/${trgovinaId}`, options3);
           if (!response.ok) {
              throw new Error(`Failed to fetch store with id ${trgovinaId}`);
           }
           const name = await response.json();
           setTrgovinaNames(prevNames => ({ ...prevNames, [trgovinaId]: name.trgovinaNaziv }));
        } catch (error) {
           console.error(`Error fetching store name for id ${trgovinaId}:`, error);
        }
     };
  }, []);

  return (
    <div id="vani2">
      <Navbar />
      {error && <p>{error}</p>}
      <div id="products-alt" className="product-section-alt">
        <div className="row">
          {products.length > 0 ? (
            products.map((product) => (
              <div 
                id="productsbox" 
                key={product.proizvodId} 
                className="col-lg-4 col-md-6 col-12 mb-3-alt"
              >
                
                  <div className="card-alt product-card-alt">
                    <img 
                      src={product.proizvodSlika} 
                      className="card-img-top-alt" 
                      alt={product.proizvod_naziv} 
                    />
                    <div className="card-body-alt">
                      <h5 className="card-title-alt">{product.proizvodNaziv}</h5>
                      <p id = "trgov">{trgovinaNames[product.trgovina]}</p>
                      <p className="card-text-alt">{product.proizvodOpis}</p>
                      <div id="gumbcijena-alt">
                        <p className="price-alt">€{product.proizvodCijena}</p>
                        <Link to={`/home/proizvod/${product.proizvodId}`} className="card-link"> 
                        <button className="pogl">Pogledaj detalje</button>
                        </Link>
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