import { Component } from '@angular/core';

/**
 * Componente : piezas de codigo que componen la aplicacion. Un componente padre puede estar formado por varios hijos.
 */
@Component({
  selector: 'app-root', /*Selector html, elemento ubicado en index.html */
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Ejemplo app basica';

  curso: string = 'Curso spring con angular';
  profesor: string = 'Nombre Profesor';
}
