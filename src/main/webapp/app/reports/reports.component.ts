import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-reports',
    templateUrl: './reports.component.html',
    styleUrls: ['reports.css']
})
export class ReportsComponent implements OnInit {
    message: string;

    constructor() {
        this.message = 'Under Development';
    }

    ngOnInit() {}
}
