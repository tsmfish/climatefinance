import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Disbursement } from './disbursement.model';
import { DisbursementPopupService } from './disbursement-popup.service';
import { DisbursementService } from './disbursement.service';

@Component({
    selector: 'jhi-disbursement-delete-dialog',
    templateUrl: './disbursement-delete-dialog.component.html'
})
export class DisbursementDeleteDialogComponent {

    disbursement: Disbursement;

    constructor(
        private disbursementService: DisbursementService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.disbursementService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'disbursementListModification',
                content: 'Deleted an disbursement'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-disbursement-delete-popup',
    template: ''
})
export class DisbursementDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private disbursementPopupService: DisbursementPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.disbursementPopupService
                .open(DisbursementDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
