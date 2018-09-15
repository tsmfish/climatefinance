import { Component, OnInit } from '@angular/core';
import { CountryCount } from '../home/countrycount';
import { GenericCount } from '../home/genericcount';
import { Validcountry } from '../home/validcountry';
import { ChartService } from 'app/charts/charts.service';
import { Router } from '@angular/router';
import { Observable } from '../../../../../node_modules/rxjs/Observable';
import { catchError } from 'rxjs/operators';

@Component({
    selector: 'jhi-charts',
    templateUrl: './charts.component.html',
    styleUrls: ['charts.css']
})
export class ChartsComponent implements OnInit {
    message: string;

    count: String;
    countryCount: GenericCount[];
    sectorCount: GenericCount[];
    detailedSectorCount: GenericCount[];
    sourceCount: GenericCount[];
    projectTypeCount: GenericCount[];
    projectStatusCount: GenericCount[];
    validCountries: Validcountry[];

    //pie chart
    viewPie: any[] = [800, 400];
    showLegend = true;
    colorScheme = {
        domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
    };
    showLabels = true;
    explodeSlices = false;
    doughnut = false;

    //bar chart
    viewBar: any[] = [1400, 700];
    showXAxis = true;
    showYAxis = true;
    gradient = false;
    showXAxisLabel = true;
    xAxisLabel = 'Country';
    showYAxisLabel = true;
    yAxisLabel = 'Population';

    //number chart
    scheme = 'cool';

    //viewNumber: any[] = [900, 300];

    constructor(private service: ChartService, private router: Router) {}

    ngOnInit() {
        this.message = 'Under Development';
        //stats
        this.getCount();
        this.getCountryCountChart();
        this.getSectorCount();
        this.getSourceCount();
        this.getProjectTypeCount();
        this.getValidCountries();
        this.getDetailedSectorCount();
        this.getProjectStatusCount();
    }

    filterCountry(countryId: any) {
        //alert(countryId);

        if (countryId == '*') {
            this.getCount();
            this.getCountryCountChart();
            this.getSectorCount();
            this.getSourceCount();
            this.getProjectTypeCount();
            this.getDetailedSectorCount();
            this.getProjectStatusCount();
        } else {
            this.service.getCountByCountry(countryId).subscribe(count => (this.count = count));
            this.service.getCountryCountChartByCountry(countryId).subscribe(countryCount => (this.countryCount = countryCount));
            this.service.getSectorCountByCountry(countryId).subscribe(sectorCount => (this.sectorCount = sectorCount));
            this.service
                .getDetailedSectorCountByCountry(countryId)
                .subscribe(detailedSectorCount => (this.detailedSectorCount = detailedSectorCount));
            this.service.getSourceCountByCountry(countryId).subscribe(sourceCount => (this.sourceCount = sourceCount));
            this.service.getProjectTypeCountByCountry(countryId).subscribe(projectTypeCount => (this.projectTypeCount = projectTypeCount));
            this.service
                .getProjectStatusCountByCountry(countryId)
                .subscribe(projectStatusCount => (this.projectStatusCount = projectStatusCount));
        }
    }

    getCount(): void {
        this.service.getCount().subscribe(count => (this.count = count));
    }

    getCountryCountChart(): void {
        this.service.getCountryCountChart().subscribe(countryCount => (this.countryCount = countryCount));
    }

    getSectorCount(): void {
        this.service.getSectorCount().subscribe(sectorCount => (this.sectorCount = sectorCount));
    }

    getDetailedSectorCount(): void {
        this.service.getDetailedSectorCount().subscribe(detailedSectorCount => (this.detailedSectorCount = detailedSectorCount));
    }

    getSourceCount(): void {
        this.service.getSourceCount().subscribe(sourceCount => (this.sourceCount = sourceCount));
    }

    getProjectTypeCount(): void {
        this.service.getProjectTypeCount().subscribe(projectTypeCount => (this.projectTypeCount = projectTypeCount));
    }

    getProjectStatusCount(): void {
        this.service.getProjectStatusCount().subscribe(projectStatusCount => (this.projectStatusCount = projectStatusCount));
    }

    getValidCountries(): void {
        this.service.getValidCountries().subscribe(validCountries => (this.validCountries = validCountries));
    }

    //events
    onSelectCountry(data) {
        //alert(data.name);
        this.router.navigateByUrl('/project;search=country.name:' + data.name);
    }

    onSelectSector(data) {
        //alert(data.name);
        this.router.navigateByUrl('/project;search=sector.name:' + data.name);
    }

    onSelectDetailedSector(data) {
        //alert(data.name);
        this.router.navigateByUrl('/project;search=detailedSector.name:' + data.name);
    }

    onSelectSource(data) {
        //alert(data.name);
        this.router.navigateByUrl('/project;search=principalSource:' + data.name);
    }

    onSelectProjectType(data) {
        //alert(data.name);
        this.router.navigateByUrl('/project;search=projectType:' + data.name);
    }

    onSelectProjectStatus(data) {
        //alert(data.name);
        this.router.navigateByUrl('/project;search=status:' + data.name);
    }
}
