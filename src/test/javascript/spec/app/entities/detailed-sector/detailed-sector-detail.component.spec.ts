/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClimatefinanceTestModule } from '../../../test.module';
import { DetailedSectorDetailComponent } from 'app/entities/detailed-sector/detailed-sector-detail.component';
import { DetailedSector } from 'app/shared/model/detailed-sector.model';

describe('Component Tests', () => {
    describe('DetailedSector Management Detail Component', () => {
        let comp: DetailedSectorDetailComponent;
        let fixture: ComponentFixture<DetailedSectorDetailComponent>;
        const route = ({ data: of({ detailedSector: new DetailedSector(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ClimatefinanceTestModule],
                declarations: [DetailedSectorDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(DetailedSectorDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(DetailedSectorDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.detailedSector).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
