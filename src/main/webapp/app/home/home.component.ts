import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { HomeService } from './home.service';
import { Validcountry } from './validcountry';
import { GenericCount } from './genericcount';
import { CountryCount } from './countrycount';
import { LoginModalService, Principal, Account } from 'app/core';
import { Router } from '@angular/router';
import { ValueCount } from 'app/home/valuecount';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    count: String;
    countryCount: CountryCount[];
    countryCountChart: GenericCount[];
    countryValueChart: ValueCount[];

    // number chart
    viewNumber: any[] = [undefined, 160];

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private service: HomeService,
        private router: Router
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();

        this.getCount();
        this.getCountryCount();
        this.getCountryCountChart();
        this.getCountryValueChart();
    }

    getCount(): void {
        this.service.getCount().subscribe(count => (this.count = count));
    }

    getCountryCount(): void {
        this.service.getCountryCount().subscribe(countryCount => (this.countryCount = countryCount));
    }

    getCountryCountChart(): void {
        this.service.getCountryCountChart().subscribe(countryCountChart => (this.countryCountChart = countryCountChart));
    }

    getCountryValueChart(): void {
        this.service.getCountryValueChart().subscribe(countryValueChart => (this.countryValueChart = countryValueChart));
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;

                this.getCount();
                this.getCountryCount();
                this.getCountryCountChart();
                this.getCountryValueChart();
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    onSelectCountry(data) {
        this.router.navigateByUrl('/project;search=country.name:' + data.name.replace('Islands', '').trim());
    }
}
