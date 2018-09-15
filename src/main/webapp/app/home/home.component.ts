import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { HomeService } from './home.service';
import { Validcountry } from './validcountry';
import { GenericCount } from './genericcount';
import { CountryCount } from './countrycount';
import { LoginModalService, Principal, Account } from 'app/core';
import { Router } from '@angular/router';

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

    //number chart
    scheme = 'cool';
    viewNumber: any[] = [860, 200];
    bandColor = 'lightgreen';

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
        //stats
        this.getCount();
        this.getCountryCount();
        this.getCountryCountChart();
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

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
                //stats
                this.getCount();
                this.getCountryCount();
                this.getCountryCountChart();
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    //events
    //events
    onSelectCountry(data) {
        //alert(data.name);
        this.router.navigateByUrl('/project;search=country.name:' + data.name.replace('Islands', '').trim());
    }
}
