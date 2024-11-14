//stranica koja se otvori u slucaju krive putanje 

export function NotFound() {
   return ( 
      <div>
         <h1>Stranica koju tražite ne postoji ili niste prijavljeni da vidite njen sadržaj!</h1>
         <a href="/">Nazad na prijavu?</a>
      </div>
   );
}