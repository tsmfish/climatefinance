import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Disbursement } from './disbursement.model';
import { DisbursementService } from './disbursement.service';

@Component({
    selector: 'jhi-disbursement-detail',
    templateUrl: './disbursement-detail.component.html'
})
export class DisbursementDetailComponent implements OnInit, OnDestroy {

    disbursement: Disbursement;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private disbursementService: DisbursementService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDisbursements();
    }

    load(id) {
        this.disbursementService.find(id)
            .subscribe((disbursementResponse: HttpResponse<Disbursement>) => {
                this.disbursement = disbursementResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDisbursements() {
        this.eventSubscriber = this.eventManager.subscribe(
            'disbursementListModification',
            (response) => this.load(this.disbursement.id)
        );
    }
}
