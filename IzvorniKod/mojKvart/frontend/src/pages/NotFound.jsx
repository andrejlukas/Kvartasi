//stranica koja se otvori u slucaju krive putanje 

export function NotFound() {
   return ( 
      <div>
         <h2>Stranica koju tražite ne postoji ili niste prijavljeni da vidite njen sadržaj!</h2>
         <h3><a href="/">Nazad na prijavu?</a></h3>
      </div>
   );
}