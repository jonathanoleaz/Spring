import { Component, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { Cliente } from './cliente';
import { ClienteService } from './cliente.service';

@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html'
})
export class ClientesComponent implements OnInit {

  clientes: Cliente[]=[];

  constructor(private clienteService: ClienteService) { }

  /**
   * Evento llamado cuando se inicia el componente
   */
  ngOnInit() {
    this.clienteService.getClientes().subscribe(
      (clientes) => {
        this.clientes = clientes
      }
    );
  }

  delete(cliente: Cliente): void{
    Swal.fire({
      title: 'Confirmacion',
      text: `¿Desea eliminar el cliente ${cliente.nombre} ${cliente.apellido}?`,
      showCancelButton: true,
      confirmButtonText: 'Aceptar',
      cancelButtonText: 'Rechazar',
      reverseButtons: true
    }).then((result) =>{
      if(result.value){
        this.clienteService.delete(cliente.id).subscribe(
          response => {
            this.clientes = this.clientes.filter(cli => cli != cliente)
            Swal.fire('Borrado', 'Borrado', 'success');
          }
        );
        
      }
    })
  }

}
