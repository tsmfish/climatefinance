/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DisbursementDetailComponent } from 'app/entities/disbursement/disbursement-detail.component';
import { Disbursement } from 'app/shared/model/disbursement.model';

describe('Component Tests', () => {
    describe('Disbursement Management Detail Component', () => {
        let comp: DisbursementDetailComponent;
        let fixture: ComponentFixture<DisbursementDetailComponent>;
        const route = ({ data: of({ disbursement: new Disbursement(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DisbursementDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DisbursementDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DisbursementDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.disbursement).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
