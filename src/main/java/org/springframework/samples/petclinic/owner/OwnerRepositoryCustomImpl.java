package org.springframework.samples.petclinic.owner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// この実装クラスの名前は「元のリポジトリ名 + CustomImpl」（例: OwnerRepositoryCustomImpl）
// または「カスタムインターフェース名 + Impl」（例: OwnerRepositoryCustomImpl）
// の規則に従う必要があります。Spring Data JPAが自動的にこれを検出して結びつけます。
// @Repository や @Component アノテーションは通常不要です。
public class OwnerRepositoryCustomImpl implements OwnerRepositoryCustom {

	// EntityManager を注入して、データベースに対する操作を行います
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = true) // 読み取り専用トランザクションを適用
	public Page<Owner> findByLastNameManual(String lastName, Pageable pageable) {
		System.out.println("Vulnerable method 1");
		// --- ここで手動で SELECT 文（JPQL）を記述します ---
		// ユーザー入力値（:lastName）を直接文字列連結せず、プレースホルダーを使用することが重要です。
		// これによりSQLインジェクション攻撃を防ぎます。
		String jpql = "SELECT DISTINCT owner FROM Owner owner LEFT JOIN owner.pets WHERE owner.lastName LIKE :lastName ORDER BY owner.lastName";
		String jpql2 = "SELECT DISTINCT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE '"
				+ lastName + "%'";

		// ページングのための総件数取得クエリ
		String countJpql = "SELECT COUNT(DISTINCT owner) FROM Owner owner LEFT JOIN owner.pets WHERE owner.lastName LIKE :lastName";

		// --------------------------------------------------

		// データ取得クエリの準備とパラメータバインド
		TypedQuery<Owner> query = entityManager.createQuery(jpql2, Owner.class);
		// query.setParameter("lastName", lastName + "%"); // LIKE検索のために '%' を追加

		// ページングの適用
		query.setFirstResult((int) pageable.getOffset()); // 開始位置
		query.setMaxResults(pageable.getPageSize()); // 取得件数

		List<Owner> owners = query.getResultList(); // クエリを実行して結果リストを取得

		// 総件数クエリの準備とパラメータバインド
		TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
		countQuery.setParameter("lastName", lastName + "%");
		long total = countQuery.getSingleResult(); // 総件数を取得

		// Page オブジェクトを構築して返却
		return new PageImpl<>(owners, pageable, total);
	}

}