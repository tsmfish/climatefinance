import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-about',
    templateUrl: './about.component.html',
    styleUrls: ['about.css']
})
export class AboutComponent implements OnInit {
    message: string;

    constructor() {
        this.message = 'In Development';
    }

    ngOnInit() {}
}
