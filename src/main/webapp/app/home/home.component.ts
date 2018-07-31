import {Component, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Account, LoginModalService, Principal} from '../shared';

import {HomeService} from './home.service';
import {Validcountry} from './validcountry';
import {GenericCount} from './genericcount';
import {CountryCount} from './countrycount';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private service: HomeService,
    ) {
    }


    count: String;
    countryCounts: CountryCount[];


    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();

        //stats
        this.getCount();
        this.getCountryCount();
    }

    getCount(): void {
        this.service.getCount().subscribe(count => this.count = count);
    }

    getCountryCount(): void {
        this.service.getCountryCount().subscribe(countryCounts => this.countryCounts = countryCounts);
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
