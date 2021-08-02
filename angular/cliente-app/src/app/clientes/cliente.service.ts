import { Injectable } from '@angular/core';
import { Cliente } from './cliente';
import { CLIENTES } from './clientes.json';
import { of, Observable, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, catchError, tap } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { formatDate, registerLocaleData } from '@angular/common';


@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private urlEndpoint: string = 'http://localhost:8080/api/clientes';

  private httpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' })
  constructor(private http: HttpClient, private router: Router) { }

  getClientes(): Observable<Cliente[]> {
    //return of(CLIENTES);
    return this.http.get(this.urlEndpoint).pipe(
       /*Tap NO afecta el tipo de dato de response*/
      tap(response=>{
        let clientes = response as Cliente[];
        console.log("Tap1 desde componente de cliente servicio");
        clientes.forEach(cliente =>{
          console.log(cliente.nombre);
        }

        )
      }),
      /*Map afecta el tipo de dato de response: de aqui en adelante, response sera un array de Cliente */
      map((response) => {
        let clientes = response as Cliente[];

        return clientes.map(cliente => {

          cliente.nombre = cliente.nombre.toLocaleUpperCase();
          cliente.createAt = formatDate(cliente.createAt, 'EEE dd/MMMM/YYYY', 'es-MX');
          return cliente;
        });
      }
      ),

      tap(response=>{
        console.log("Tap2 desde componente de cliente servicio");
        response.forEach(cliente =>{
          console.log(cliente.nombre);
        }

        )
      }),
    );
  }

  create(cliente: Cliente): Observable<any> {
    return this.http.post<any>(this.urlEndpoint, cliente, { headers: this.httpHeaders }).pipe(
      catchError(e => {

        if (e.status == 400) {
          console.log(e);
          return throwError(e);

        }
        Swal.fire("Error", "Error al crear: " + e.error.mensaje, "warning");
        /*Se debe regresar un objeto observable */
        return throwError(e);
      })
    )
  }


  getCliente(id: any): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.urlEndpoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/clientes']);
        Swal.fire("Error", "Error al obtener cliente: " + e.error.mensaje, "warning");
        return throwError(e);
      })
    )
  }

  update(cliente: Cliente): Observable<any> {
    return this.http.put<any>(`${this.urlEndpoint}/${cliente.id}`, cliente, { headers: this.httpHeaders }).pipe(
      catchError(e => {
        if (e.status == 400) {
          throwError(e);
        }
        Swal.fire("Error", "Error al editar: " + e.error.mensaje, "warning");
        return throwError(e);
      })
    )
  }

  delete(id: number): Observable<Cliente> {
    return this.http.delete<Cliente>(`${this.urlEndpoint}/${id}`, { headers: this.httpHeaders }).pipe(
      catchError(e => {
        Swal.fire("Error", "Error al editar: " + e.error.mensaje, "warning");
        return throwError(e);
      })
    );
  }
}
