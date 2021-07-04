import { Component } from "@angular/core";

/**
 * Se decora con Component, es decir, segun el rol que cumple para poder "inyectarlo"
 */
@Component({
    selector: 'app-header',
    templateUrl: './header.component.html'
})
export class HeaderComponent {
    title: string = 'App Angular con Spring'
}