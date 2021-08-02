import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Cliente } from './cliente';
import { ClienteService } from './cliente.service';
import swal from 'sweetalert2';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements OnInit {
  public cliente: Cliente = new Cliente();
  public titulo: string = "Crear cliente";
  public errores: string[];

  constructor(
    private clienteService: ClienteService,
    private router: Router,
    private activateRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.cargarCliente();
  }

  cargarCliente(): void {
    this.activateRoute.params.subscribe(params => {
      let id = params['id'];
      if (id) {
        this.clienteService.getCliente(id).subscribe((cliente) => this.cliente = cliente)
      }
    })
  }

  public create(): void {
    this.clienteService.create(this.cliente).subscribe(
      /*Se dirige en la vista a '/clientes' */
      json => {
        this.router.navigate(['/clientes']);
        swal.fire('Cliente creado', `${json.mensaje} ${json.cliente.nombre}`, 'success');
      },
      err => {
        this.errores = err.error.err as string[];
      }
    )
  }

  update(): void {
    this.clienteService.update(this.cliente)
      .subscribe(json => {
        this.router.navigate(['/clientes']);
        swal.fire('Cliente actualizado', `${json.mensaje} ${json.cliente.nombre}`, 'success');
      })
  }

}