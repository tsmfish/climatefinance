import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDisbursement } from 'app/shared/model/disbursement.model';

type EntityResponseType = HttpResponse<IDisbursement>;
type EntityArrayResponseType = HttpResponse<IDisbursement[]>;

@Injectable({ providedIn: 'root' })
export class DisbursementService {
    private resourceUrl = SERVER_API_URL + 'api/disbursements';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/disbursements';

    constructor(private http: HttpClient) {}

    create(disbursement: IDisbursement): Observable<EntityResponseType> {
        return this.http.post<IDisbursement>(this.resourceUrl, disbursement, { observe: 'response' });
    }

    update(disbursement: IDisbursement): Observable<EntityResponseType> {
        return this.http.put<IDisbursement>(this.resourceUrl, disbursement, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDisbursement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDisbursement[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDisbursement[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
