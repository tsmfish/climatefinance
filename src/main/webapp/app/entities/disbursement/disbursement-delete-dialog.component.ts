import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDisbursement } from 'app/shared/model/disbursement.model';
import { DisbursementService } from './disbursement.service';

@Component({
    selector: 'jhi-disbursement-delete-dialog',
    templateUrl: './disbursement-delete-dialog.component.html'
})
export class DisbursementDeleteDialogComponent {
    disbursement: IDisbursement;

    constructor(
        private disbursementService: DisbursementService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.disbursementService.delete(id).subscribe(response => {
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
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ disbursement }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(DisbursementDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.disbursement = disbursement;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
