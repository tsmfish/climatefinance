import { Component, OnInit } from '@angular/core';
import { Validcountry } from 'app/home/validcountry';
import { ChartService } from 'app/charts/charts.service';

@Component({
    selector: 'jhi-reports',
    templateUrl: './reports.component.html',
    styleUrls: ['reports.css']
})
export class ReportsComponent implements OnInit {
    message: string;

    constructor(private service: ChartService) {
        this.message = 'Under Development';
    }

    validCountries: Validcountry[];

    ngOnInit() {
        this.getValidCountries();
    }

    getValidCountries(): void {
        this.service.getValidCountries().subscribe(validCountries => (this.validCountries = validCountries));
    }
}
